package org.sample.checkers.importer;


import javafx.beans.InvalidationListener;
import javafx.collections.ObservableFloatArray;
import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.transform.Affine;
import javafx.scene.transform.MatrixType;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.scene.transform.Transform;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * PolygonMesh that knows how to update itself given changes in joint transforms.
 * The mesh can be updated with an AnimationTimer.
 */
public final class SkinningMesh extends PolygonMesh {
    private final float[][] relativePoints; // nJoints x nPoints*3
    private final float[][] weights; // nJoints x nPoints
    private final List<Integer>[] weightIndices;
    private final List<JointIndex> jointIndexForest;
    private boolean jointsTransformDirty = true;
    private Transform bindGlobalInverseTransform;
    private final Transform[] jointToRootTransforms; // the root refers to the group containing all the mesh skinning nodes (i.e. the parent of jointForest)
    private final int nPoints;
    private final int nJoints;


    /**
     * SkinningMesh constructor
     *
     * @param mesh                The binding mesh
     * @param weights             A two-dimensional array (nJoints x nPoints) of the influence weights used for skinning
     * @param bindTransforms      The binding transforms for every joint
     * @param bindGlobalTransform The global binding transform; all binding transforms are defined with respect to this frame
     * @param joints              A list of joints used for skinning; the order of these are associated with the respective attributes of @weights and @bindTransforms
     * @param jointForest         A list of the top level trees that contain the joints; all the @joints should be contained in this forest
     */
    public SkinningMesh(final PolygonMesh mesh, final float[][] weights, final Affine[] bindTransforms, final Affine bindGlobalTransform,
                        final List<Joint> joints, final List<Parent> jointForest) {//, boolean transformsAreRelatives) {
        this.getPoints().addAll(mesh.getPoints());
        this.getTexCoords().addAll(mesh.getTexCoords());
        this.faces = mesh.faces;
        this.getFaceSmoothingGroups().addAll(mesh.getFaceSmoothingGroups());

        this.weights = weights;

        nJoints = joints.size();
        nPoints = getPoints().size() / getPointElementSize();

        // Create the jointIndexForest forest. Its structure is the same as
        // jointForest, except that this forest have indices information and
        // some branches are pruned if they don't contain joints.
        jointIndexForest = new ArrayList<>(jointForest.size());
        for (Parent jointRoot : jointForest) {
            jointIndexForest.add(new JointIndex(jointRoot, joints.indexOf(jointRoot), joints));
        }

        try {
            bindGlobalInverseTransform = bindGlobalTransform.createInverse();
        } catch (NonInvertibleTransformException ex) {
            System.err.println("Caught NonInvertibleTransformException: " + ex.getMessage());
        }

        jointToRootTransforms = new Transform[nJoints];

        // For optimization purposes, store the indices of the non-zero weights
        weightIndices = new List[nJoints];
        for (int j = 0; j < nJoints; j++) {
            weightIndices[j] = new ArrayList<>();
            for (int i = 0; i < nPoints; i++) {
                if (weights[j][i] != 0.0f) {
                    weightIndices[j].add(i);
                }
            }
        }

        // Compute the points of the binding mesh relative to the binding transforms
        final ObservableFloatArray points = getPoints();
        relativePoints = new float[nJoints][nPoints * 3];
        for (int j = 0; j < nJoints; j++) {
            Transform postBindTransform = bindTransforms[j].createConcatenation(bindGlobalTransform);
            for (int i = 0; i < nPoints; i++) {
                final Point3D relativePoint = postBindTransform.transform(points.get(3 * i), points.get(3 * i + 1), points.get(3 * i + 2));
                relativePoints[j][3 * i] = (float) relativePoint.getX();
                relativePoints[j][3 * i + 1] = (float) relativePoint.getY();
                relativePoints[j][3 * i + 2] = (float) relativePoint.getZ();
            }
        }

        // Add a listener to all the joints (and their parents nodes) so that we can track when any of their transforms have changed
        // Set of joints that already have a listener (so we don't attach a listener to the same node more than once)
        final Set<Node> processedNodes = new HashSet<>(joints.size());
        final InvalidationListener invalidationListener = observable -> jointsTransformDirty = true;
        for (Joint joint : joints) {
            Node node = joint;
            while (!processedNodes.contains(node)) {
                node.localToParentTransformProperty().addListener(invalidationListener);
                processedNodes.add(node);
                // Don't check for nodes above the jointForest
                if (jointForest.contains(node) || node.parentProperty().isNull().get()) {
                    break;
                }
                node = node.getParent();
            }
        }
    }

    private final class JointIndex {
        final Node node;
        final int index;
        final List<JointIndex> children = new ArrayList<>();
        JointIndex parent = null;
        Transform localToGlobalTransform;

        public JointIndex(final Node n, final int ind, final List<Joint> orderedJoints) {
            node = n;
            index = ind;
            if (node instanceof Parent) {
                for (Node childJoint : ((Parent) node).getChildrenUnmodifiable()) {
                    if (childJoint instanceof Parent) { // is childJoint a joint or a node with children?
                        final int childInd = orderedJoints.indexOf(childJoint);
                        final JointIndex childJointIndex = new JointIndex(childJoint, childInd, orderedJoints);
                        childJointIndex.parent = this;
                        children.add(childJointIndex);
                    }
                }
            }
        }
    }

    // Updates the jointToRootTransforms by doing a a depth-first search of the jointIndexForest
    private void updateLocalToGlobalTransforms(final List<JointIndex> jointIndexForest) {
        for (final JointIndex jointIndex : jointIndexForest) {
            if (jointIndex.parent == null) {
                jointIndex.localToGlobalTransform = bindGlobalInverseTransform.createConcatenation(jointIndex.node.getLocalToParentTransform());
            } else {
                jointIndex.localToGlobalTransform = jointIndex.parent.localToGlobalTransform.createConcatenation(jointIndex.node.getLocalToParentTransform());
            }
            if (jointIndex.index != -1) {
                jointToRootTransforms[jointIndex.index] = jointIndex.localToGlobalTransform;
            }
            updateLocalToGlobalTransforms(jointIndex.children);
        }
    }

    // Updates its points only if any of the joints' transforms have changed
    public final void update() {
        if (!jointsTransformDirty) {
            return;
        }

        updateLocalToGlobalTransforms(jointIndexForest);

        final float[] points = new float[nPoints * 3];
        double[] t = new double[12];
        float[] relativePoint;
        for (int j = 0; j < nJoints; j++) {
            jointToRootTransforms[j].toArray(MatrixType.MT_3D_3x4, t);
            relativePoint = relativePoints[j];
            for (Integer i : weightIndices[j]) {
                points[3 * i] += weights[j][i] * (t[0] * relativePoint[3 * i] + t[1] * relativePoint[3 * i + 1] + t[2] * relativePoint[3 * i + 2] + t[3]);
                points[3 * i + 1] += weights[j][i] * (t[4] * relativePoint[3 * i] + t[5] * relativePoint[3 * i + 1] + t[6] * relativePoint[3 * i + 2] + t[7]);
                points[3 * i + 2] += weights[j][i] * (t[8] * relativePoint[3 * i] + t[9] * relativePoint[3 * i + 1] + t[10] * relativePoint[3 * i + 2] + t[11]);
            }
        }
        getPoints().set(0, points, 0, points.length);

        jointsTransformDirty = false;
    }
}
