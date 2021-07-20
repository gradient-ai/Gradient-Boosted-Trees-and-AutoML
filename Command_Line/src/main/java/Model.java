package h2odemo;

import hex.genmodel.MojoModel;
import hex.genmodel.easy.EasyPredictModelWrapper;
import hex.genmodel.easy.RowData;
import hex.genmodel.easy.exception.PredictException;
import hex.genmodel.easy.prediction.BinomialModelPrediction;
import java.io.File;
import java.io.IOException;

class Model {
    private MojoModel model = null;
    private EasyPredictModelWrapper predictModel = null;

    public Model(String modelPath) throws IOException {
        if(new File(modelPath).isFile()){
            try{
                this.model = MojoModel.load(modelPath);
                this.predictModel = new EasyPredictModelWrapper(this.model);
            }
            catch (IOException e) {
                throw new RuntimeException("Invalid H2O MOJO file");
            }
        } else {
            throw new RuntimeException("No valid file for path: "+modelPath);
        }
    }

    public String predict(RowData input) throws PredictException {
        try{
            BinomialModelPrediction prediction = predictModel.predictBinomial(input);
            return prediction.label;
        }
        catch (PredictException e){
            return "Exception!";
        }
    }
}
