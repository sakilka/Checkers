package org.sample.checkers.loader.loaded;

import org.sample.checkers.loader.model.Model;

public class ModelFactory {

    public static Model getModel(String modelName){

        switch (modelName){
            case "bishop":
                return new BishopModel();
            case "king":
                return new KingModel();
            case "knight":
                return new KnightModel();
            case "pawn":
                return new PawnModel();
            case "queen":
                return new QueenModel();
            case "rook":
            default:
                return new RookModel();
        }
    }
}
