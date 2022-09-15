package org.sample.checkers.config;

import org.sample.checkers.loader.ObjLoader;
import org.sample.checkers.loader.model.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class LoadModel {

    Map<String, Model> model;

    @Autowired
    public LoadModel(@Value("org/sample/checkers/board/model/bishop.obj")Resource bishop,
                     @Value("org/sample/checkers/board/model/king.obj")Resource king,
                     @Value("org/sample/checkers/board/model/knight.obj")Resource knight,
                     @Value("org/sample/checkers/board/model/pawn.obj")Resource pawn,
                     @Value("org/sample/checkers/board/model/queen.obj")Resource queen,
                     @Value("org/sample/checkers/board/model/rook.obj")Resource rook)
            throws InterruptedException, ExecutionException, TimeoutException {

        model = new HashMap<>(6);

        ExecutorService executor = Executors.newCachedThreadPool();
        List<Callable<Model>> tasks = new ArrayList<>();
        for (final Resource resource: Stream.of(bishop, king, knight, queen, pawn, rook).collect(Collectors.toList())) {
            Callable<Model> c = () -> ObjLoader.loadModel(resource.getFile());
            tasks.add(c);
        }

        List<Future<Model>> results = executor.invokeAll(tasks);

        for (Future<Model> result : results){
            Model resultModel = result.get(10, TimeUnit.SECONDS);
            model.put(resultModel.modelName, resultModel);
        }

        executor.shutdown();
    }

    public Map<String, Model> getModel() {
        return model;
    }
}
