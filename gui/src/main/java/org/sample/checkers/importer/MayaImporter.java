package org.sample.checkers.importer;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.DepthTest;
import javafx.scene.Node;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * MayaImporter
 * <p/>
 * MayaImporter.getRoot() returns a JavaFX node hierarchy MayaImporter.getTimeline() returns a JavaFX timeline
 */
public class MayaImporter extends Importer {
    public static final boolean DEBUG = Loader.DEBUG;
    public static final boolean WARN = Loader.WARN;

    // NO_JOINTS
    // [Note to Alex]: I've re-enabled joints, but lets not use rootCharacter [John]
    // javafx.scene.shape3d.Character rootCharacter = new javafx.scene.shape3d.Character();
    MayaGroup root = new MayaGroup();
    Timeline timeline;
    Set<Node> meshParents = new HashSet();

    // NO_JOINTS
    // [Note to Alex]: I've re-enabled joints, but lets not use rootCharacter [John]
    // public javafx.scene.shape3d.Character getRootCharacter() {
    //        return rootCharacter;
    // }

    @Override
    public MayaGroup getRoot() {
        return root;
    }

    //=========================================================================
    // MayaImporter.getTimeline
    //-------------------------------------------------------------------------
    // MayaImporter.getTimeline() returns a JavaFX timeline
    // (javafx.animation.Timeline)
    //=========================================================================
    @Override
    public Timeline getTimeline() {
        return timeline;
    }

    //=========================================================================
    // MayaImporter.getMeshParents
    //=========================================================================
    public Set<Node> getMeshParents() {
        return meshParents;
    }

    //=========================================================================
    // MayaImporter.load
    //=========================================================================
    @Override
    public void load(String url, boolean asPolygonMesh) {
        try {
            Loader loader = new Loader();
            loader.load(new java.net.URL(url), asPolygonMesh);
            // This root is not automatically added to the scene.
            // It needs to be added by the user of MayaImporter.
            //            root = new Xform();

            // Add top level nodes to the root
            int nodeCount = 0;
            for (Node n : loader.loaded.values()) {
                if (n != null) {
                    // Only add a node if it has no parents, ie. top level node
                    if (n.getParent() == null) {
                        if (Loader.DEBUG) {
                            System.out.println("Adding top level node " + n.getId() + " to root!");
                        }
                        n.setDepthTest(DepthTest.ENABLE);
                        if (!(n instanceof MeshView) || ((TriangleMesh) ((MeshView) n).getMesh()).getPoints().size() > 0) {
                            root.getChildren().add(n);
                        }
                    }
                    nodeCount++;
                }
            }
            // [Note to Alex]: I've re-enabled joints, but lets not use rootCharacter [John]
            // rootCharacter.setRootJoint(loader.rootJoint);
            if (Loader.DEBUG) {
                System.out.println("There are " + nodeCount + " nodes.");
            }

            // if meshes were not loaded in the code above
            // (which they now are) one would need to
            // set meshParents from the loader
            // meshParents.addAll(loader.meshParents.keySet());
            // this is not necessary at the moment

            timeline = new Timeline();
            int count = 0;

            // Add all the keyframes to the timeline from loader.keyFrameMap
            for (final Map.Entry<Float, List<KeyValue>> e : loader.keyFrameMap.entrySet()) {
                // if (DEBUG) System.out.println("key frame at : "+ e.getKey());
                timeline.getKeyFrames().add
                        (
                                new KeyFrame(
                                        javafx.util.Duration.millis(e.getKey() * 1000f),
                                        (KeyValue[]) e.getValue().toArray(new KeyValue[e.getValue().size()])));
                count++;
            }

            if (Loader.DEBUG) {
                System.out.println("Loaded " + count + " key frames.");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isSupported(String extension) {
        return extension != null && extension.equals("ma");
    }
}