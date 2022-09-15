package org.sample.checkers.config;

import org.sample.checkers.loader.ObjLoader;
import org.sample.checkers.loader.model.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class LoadModel {

    Map<String, Model> model;

    @Autowired
    public LoadModel(@Value("org/sample/checkers/board/model/bishop.obj")Resource bishop,
                     @Value("org/sample/checkers/board/model/king.obj")Resource king,
                     @Value("org/sample/checkers/board/model/knight.obj")Resource knight,
                     @Value("org/sample/checkers/board/model/pawn.obj")Resource pawn,
                     @Value("org/sample/checkers/board/model/queen.obj")Resource queen,
                     @Value("org/sample/checkers/board/model/rook.obj")Resource rook) throws IOException {
        model = new HashMap<>(6);

        model.put("bishop", ObjLoader.loadModel(bishop.getFile()));
        model.put("king", ObjLoader.loadModel(king.getFile()));
        model.put("knight", ObjLoader.loadModel(knight.getFile()));
        model.put("pawn", ObjLoader.loadModel(pawn.getFile()));
        model.put("queen", ObjLoader.loadModel(queen.getFile()));
        model.put("rook", ObjLoader.loadModel(rook.getFile()));
    }

    public Map<String, Model> getModel() {
        return model;
    }
}
