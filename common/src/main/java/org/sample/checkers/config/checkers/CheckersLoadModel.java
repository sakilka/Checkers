package org.sample.checkers.config.checkers;

import org.sample.checkers.loader.ObjLoader;
import org.sample.checkers.loader.model.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class CheckersLoadModel {

    Map<String, Model> model;

    @Autowired
    public CheckersLoadModel(@Value("org/sample/checkers/chess/board/model/pawn.obj")Resource pawn,
                          @Value("org/sample/checkers/chess/board/model/queen.obj")Resource queen)
            throws InterruptedException, ExecutionException, TimeoutException {

        model = new HashMap<>(2);

        ExecutorService executor = Executors.newCachedThreadPool();
        List<Callable<Model>> tasks = new ArrayList<>();
        for (final Resource resource: Stream.of(queen, pawn).collect(Collectors.toList())) {
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
