**TODO:** Items below marked TODO before sharing publicly

## Introduction

This repository is to show an example of using non-deep-learning machine learning on Gradient. It accompanies the blog entry "Gradient Boosted Trees and AutoML" on the [Paperspace blog](https://blog.paperspace.com). (**TODO**: Link when live)

Many enterprises and other machine learning (ML) users have problems best solved by ML methods other than deep learning. This may be for reasons of interpretability, robustness to real-world data, regulatory requirements, available computing power or time, approved software, or available expertise. Gradient is able to support these approaches by enabling the use of such tools.

The project consists of 3 parts that can be run independently:

* **Notebook**: Gradient Boosted Trees and AutoML, no deployment
* **Workflow**: Same as notebook but in a Gradient Workflow
* **Command line**: Deployment of model to production

The most common successful ML on real business problems is Gradient Boosted decision trees (GBTs, not to be confused with the text models GPTs, or Paperspace Gradient :) ), which have been used extensively in the enterprise. They have also won most Kaggle competitions that were not won by deep learning.

TensorFlow and PyTorch are not state-of-the-art for ML outside of deep learning. We therefore use GBTs via the well-known open source [H2O](http://h2o.ai) library of ML models. This incorporate both XGBoost and other functionality such as [AutoML](https://docs.h2o.ai/h2o/latest-stable/h2o-docs/automl.html) (smart hyperparameter search, not no-code), GPUs, and large scale training and deployment.

Last updated: Jul 21st 2021

## To run the Notebook (basic)

Run from the Gradient ML Showcase page. (Link to follow when it is live.)

Or run the Notebook file on Gradient

 - Clone this repository to your machine: `git clone https://github.com/gradient-ai/Gradient-Boosted-Trees-and-AutoML`
 - In the Gradient GUI, create a Notebook with the following settings:
   - Name = H2O Workflow (or any allowed name)
   - Select a runtime = TensorFlow 2.4.1 (the project doesn't use TensorFlow, but this container is suitable)
   - Select a machine = C7
   - Public/private = set to preferred option
   - Advanced options = leave as default
 - Upload the file `Notebook/automl_in_h2o.ipynb` from this repo to the Gradient Notebook, along with the `income.csv` data
 - Run the Notebook in the [usual way](https://docs.paperspace.com/gradient/explore-train-deploy/notebooks)

Notebook creation can also be done on the command line if desired, via `gradient notebooks create`.

## To run the Workflow (advanced)

Gradient Workflows remain subject to some [caveats](https://docs.paperspace.com/gradient/get-started/tutorials-list/workflows-sample-project#caveats) due to not-yet-supported functionality.

With access to a Gradient Private Cluster, Workflows can be run as follows:

 - [Create a project](https://docs.paperspace.com/gradient/get-started/managing-projects#create-a-project) and optionally [get its ID](https://docs.paperspace.com/gradient/get-started/managing-projects#get-your-projects-id)
 - [Generate an API key](https://docs.paperspace.com/gradient/get-started/quick-start/install-the-cli#obtaining-an-api-key) for your project to allow access
 - [Install the Gradient CLI](https://docs.paperspace.com/gradient/get-started/quick-start/install-the-cli) on your machine
 - Use or create a [Gradient Private cluster](https://docs.paperspace.com/gradient/gradient-private-cloud/about/setup/managed-installation) and [its ID](https://docs.paperspace.com/gradient/gradient-private-cloud/about/usage#finding-your-cluster-id)
 - [Create a new Workflow](https://docs.paperspace.com/gradient/explore-train-deploy/workflows/getting-started-with-workflows#creating-gradient-workflows) via CLI or GUI and [get its ID](https://docs.paperspace.com/gradient/explore-train-deploy/workflows/getting-started-with-workflows#running-your-first-workflow-run)
 - [Create an output Dataset](https://docs.paperspace.com/gradient/data/data-overview/private-datasets-repository#creating-a-dataset-and-dataset-version) for the Workflow, in which the model can be saved
 - Add the Dataset's ID to the `gbt_automl.yaml` file in the place indicated in that file
 - [Import a placeholder file](https://docs.paperspace.com/gradient/data/data-overview/private-datasets-repository#creating-a-dataset-and-dataset-version) into the created output dataset using the GUI

You can then run the Workflow from your command line with the appropriate substitutions into

```
gradient workflows run \
  --id         <your workflow ID> \
  --clusterId  <Your private cluster ID> \
  --path       your/path/to/gbt_automl.yaml \
  --apiKey     <Your API key>
```

It will look something like

```
gradient workflows run \
  --id         abc123de-f567-ghi8-90jk-l123mno456pq \
  --clusterId  cdefghijk \
  --path       ./Workflow/gbt_automl.yaml \
  --apiKey     ab12cd34ef56gh78ij90kl12mn34op
```

The results can be viewed by navigating to the Workflows tab within the project where you created your Workflow.

## To run the model deployment (advanced)

 - Create notebook in a similar manner to the *To run the Notebook* section above, using a C5 or C7 machine
 - Open Jupyter notebook interface
 - Open terminal
 - Enter command `bash`
 - Clone this repo, which is public: `git clone https://github.com/gradient-ai/Gradient-Boosted-Trees-and-AutoML`
 - Enter command `apt-get update`
 - Install Maven: `apt install maven` (this also installs Java; answer yes to `Do you want to continue? [Y/n]` by pressing Enter)
 - You can see the versions with `mvn --version`, `java -version`, or other usual diagnostics like `which java`
 - Go to the Java directory: `cd Gradient-Boosted-Trees-and-AutoML/Command_Line`
 - Build the project: `mvn clean package` (should say `BUILD SUCCESS` close to the end of the output)
 - Run the model: `java -jar target/gbtautomlinfer-1.0-jar-with-dependencies.jar`
 - Open second terminal
 - Type `bash`
 - Send a row of inference data to the model [1]: `curl -X GET 'http://localhost:8080/predict?age=39&workclass=State-gov&fnlwgt=77516&education=Bachelors&education-num=13&marital-status=Never-married&occupation=Adm-clerical&relationship=Not-in-family&race=White&sex=Male&capital-gain=2174&capital-loss=0&hours-per-week=40'`

If all goes well the `java -jar` step will show output that looks like

![Deployed model](https://github.com/gradient-ai/Gradient-Boosted-Trees-and-AutoML/blob/main/Command_Line/deployed_model_listening.png)

and the `curl` step will return a prediction from the model for the inference data row

![Model response](https://github.com/gradient-ai/Gradient-Boosted-Trees-and-AutoML/blob/main/Command_Line/deployed_model_response.png)

You can exit the deployed model in the first terminal with `Ctrl+C`, and from the terminals and shut down the Notebook in the usual Gradient way.

[1] This row of inference data is in fact the first row of income.csv, already used in the training, but it shows the main point that the setup accepts inference data and the deployed model returns predictions. The inference data does not include the ground truth column, `yearly-income`, consistent with this being inference on new data.

### Details of the model deployment setup

The setup is based upon the example in this [blog entry](https://medium.com/spikelab/building-a-machine-learning-application-using-h2o-ai-67ce3681df9c
) by Spikelab, with several modifications for both Gradient and our particular analysis.

The Notebook and/or Workflow project sections output an H2O model in MOJO format, named like

`StackedEnsemble_AllModels_AutoML_20210621_203855.zip`

There is a single associated Java dependency, `h2o-genmodel.jar`.

We use the simple web framework Javalin to build a Java project with Maven that contains the model (`Model.java`) and an app (`App.java`) that sets the model up as a REST endpoint. This is generic to many compute environments, including Gradient.

The project was created on the command line within the Gradient container, as opposed to using an IDE such as IntelliJ or Eclipse. We also use a slightly newer version of H2O, 3.32.1.3.

The Java code is modified from the regression example online to our binary classification example, hence changing the called classes and datatypes, and the H2O artifactID from `h2o-genmodel` to `h2o-genmodel-ext-xgboost` to accomodate the XGBoost models. The inference data contains both categorical and numerical datatypes.

## Conclusions and Next steps

This project shows how to run state-of-the-art ML on Gradient outside of deep learning, using Notebooks, Workflows, and a working example of model deployment for the well-known open source library H2O. This opens up a large number of projects via analogy to the examples here.

The caveats that make the Workflows step complex at present will disappear in future as the relevant functionality is added to Gradient, an model deployment will likewise continue to become easier.

## Links & Credits

 - Blog entry (**TODO**: Link when live)
 - [Gradient](https://gradient.paperspace.com)
 - [H2O AutoML](https://docs.h2o.ai/h2o/latest-stable/h2o-docs/automl.html)
 - [H2O Java deployment example](https://medium.com/spikelab/building-a-machine-learning-application-using-h2o-ai-67ce3681df9c) on which ours is based
 - [Javalin](https://javalin.io)
 - [UCI income dataset](https://archive.ics.uci.edu/ml/datasets/census+income)
