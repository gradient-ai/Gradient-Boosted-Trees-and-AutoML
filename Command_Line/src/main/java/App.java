package h2odemo;

import io.javalin.Javalin;
import java.io.IOException;
import com.google.gson.JsonObject;
import h2odemo.Model;
import hex.genmodel.easy.RowData;

public final class App {

    public static Integer port = 8080;
    public static String modelPath = "/notebooks/Gradient-Boosted-Trees-and-AutoML/Command_Line/StackedEnsemble_AllModels_AutoML_20210621_203855.zip";

    public static void main(String[] args) throws IOException {
        Model model = new Model(modelPath);
        Javalin app = Javalin.create().start(port);

        app.get("/predict", ctx -> {
            try{
                RowData modelParams = new RowData();
                ctx.queryParamMap().forEach((param, value) -> {
                    modelParams.put(param, value.get(0));
                });
                String prediction = model.predict(modelParams);
                JsonObject json_response = new JsonObject();
                json_response.addProperty("prediction", prediction);
                json_response.addProperty("status", "ok");
                ctx.result(json_response.toString());
            } catch (Exception e) {
                JsonObject json_response = new JsonObject();
                json_response.addProperty("status", "error");
            }

        });
    }
}
