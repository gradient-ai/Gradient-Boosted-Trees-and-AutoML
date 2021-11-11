# Gradient Boosted Trees and AutoML

This repository is to show an example of using non-deep-learning machine learning on Gradient. It accompanies the blog entry [Gradient Boosted Trees and AutoML](https://blog.paperspace.com/gradient-boosted-trees-automl-h2o) on the [Paperspace blog](https://blog.paperspace.com).

Many enterprises and other machine learning (ML) users have problems best solved by ML methods other than deep learning. This may be for reasons of interpretability, robustness to real-world data, regulatory requirements, available computing power or time, approved software, or available expertise. Gradient is able to support these approaches by enabling the use of such tools.

The project and repo consist of 3 parts that can be run independently:

* **Notebook** = Gradient Boosted Trees and AutoML, no deployment
* **Workflow** = Same as notebook but in a Gradient Workflow
* **Command Line** = **Deployment** of model to production

The most common successful ML models on real business problems aside from deep learning are **gradient-boosted decision trees** (GBTs, not to be confused with the text models GPTs, or Paperspace Gradient), which have been used extensively in the enterprise. They have also won most Kaggle competitions that were not won by deep learning.

TensorFlow and PyTorch are not state-of-the-art for ML outside of deep learning. We therefore use GBTs via the well-known open source [**H2O**](http://h2o.ai) library of ML models. This incorporates both **XGBoost** and other functionality such as [**AutoML**](https://docs.h2o.ai/h2o/latest-stable/h2o-docs/automl.html) (smart hyperparameter search, not no-code), GPUs, and large scale training and deployment.

Last updated: Nov 10th 2021

## To run the Notebook (basic)

Run from this project's entry on the [Gradient ML Showcase page](https://ml-showcase.paperspace.com/projects/gradient-boosted-trees-and-automl). When the Notebook is open, navigate to the `Notebook/` directory and click `automl_in_h2o.ipynb` to open the Notebook. It can then be run in the usual way by clicking `Run` under each cell in turn.

OR

Run the Notebook file using Gradient:

 - In the Gradient GUI, create a Project with a name, e.g., Gradient Boosted Trees and AutoML
 - Within the project, create a Notebook with the following settings:
   - Don't select any of the boxes under Select a runtime
   - Select a machine = C4 [1]
   - Public/private = set to preferred option
   - Under Advanced options
     - Change the Workspace URL field from `https://github.com/gradient-ai/TensorFlow` to `https://github.com/gradient-ai/Gradient-Boosted-Trees-and-AutoML` to point to this repository. The other options can remain the same.
     - Set the Container Name to `tensorflow/tensorflow:2.4.1-gpu-jupyter` [2,3]
     - Set the Container command to `jupyter notebook --allow-root --ip=0.0.0.0 --no-browser --NotebookApp.trust_xheaders=True --NotebookApp.disable_check_xsrf=False --NotebookApp.allow_remote_access=True --NotebookApp.allow_origin='*'`
   - Start the Notebook
 - Once the Notebook has started, navigate to the `Notebook/` directory and click `automl_in_h2o.ipynb` to run the Notebook in the same way as from the Showcase
 - You can rename the notebook from "Untitled" if you want, using the GUI

Notebook creation can also be done on the command line if desired, via `gradient notebooks create`. For more details on Notebooks, see the [documentation](https://docs.paperspace.com/gradient/explore-train-deploy/notebooks).

*Notes*

[1] The model as set up is small and so doesn't benefit from the addition of a GPU such as P4000, so C4 works well. If you have access to a C7 instance, it may run faster.

[2] The project does not use TensorFlow, but this container is acceptable because we do not yet have an H2O container. Originally it was coded on `tensorflow/tensorflow:2.4.1-gpu-jupyter`, and it auto-runs on this if invoked from the Showcase. Choosing a later container such as our current recommended one will probably work but is not guaranteed, hence we fix to this one.

[3] In fact, there is a further caveat to invoking the notebook without the Showcase, which is that the `jdk.install()` line does not allow the Java minor version to be fixed, so over time newer versions will be installed. So far this has been fine, but non-fixed versions of software could eventually break. This could be solved by defining a container containing Java, so that it doesn't have to be installed from the Notebook.

## To run the Workflow (advanced)

A Workflow for this project can be run as follows:

 - [Create a project](https://docs.paperspace.com/gradient/get-started/managing-projects#create-a-project) and optionally [get its ID](https://docs.paperspace.com/gradient/get-started/managing-projects#get-your-projects-id)
 - [Generate an API key](https://docs.paperspace.com/gradient/get-started/quick-start/install-the-cli#obtaining-an-api-key) for your project to allow access, using Team settings under the GUI top-right dropdown menu
 - [Install the Gradient CLI](https://docs.paperspace.com/gradient/get-started/quick-start/install-the-cli) on your machine
 - [Create a new Workflow](https://docs.paperspace.com/gradient/explore-train-deploy/workflows/getting-started-with-workflows#creating-gradient-workflows) and [get its ID](https://docs.paperspace.com/gradient/explore-train-deploy/workflows/getting-started-with-workflows#create-a-workflow)
 - [Create an output Dataset](https://docs.paperspace.com/gradient/data/data-overview/private-datasets-repository#creating-a-dataset-and-dataset-version) named `gbt-automl` for the Workflow, in which the model can be saved

The create Workflow and create output Dataset steps can be done via the GUI or CLI. The CLI commands look like

`gradient workflows create --name GBT-AutoML --projectId <your project ID> --apiKey <your API key>`  
`gradient storageProviders list --apiKey <your API key>`

Note the `storage provider ID` of the **Gradient Managed** storage provider.

`gradient datasets create --name gbt-automl --storageProviderId <your storage provider ID> --apiKey <your API key>`

The usage of `--apiKey` on the command line is optional: you can also store a key in a JSON file, e.g., `~/.paperspace/config.json` to avoid having to add it to each command. Here we leave it present. Another option is the environment variable `PAPERSPACE_API_KEY`. See [connecting your account](https://docs.paperspace.com/gradient/get-started/quick-start/install-the-cli#connecting-your-account) for more details.

 - Run the Workflow from your command line with the appropriate substitutions into the following:

```
gradient workflows run \
  --id         <your workflow ID> \
  --path       your/path/to/gbt_automl.yaml \
  --apiKey     <Your API key>
```

It will look something like

```
gradient workflows run \
  --id         abc123de-f567-ghi8-90jk-l123mno456pq \
  --path       ./Workflow/gbt_automl.yaml \
  --apiKey     ab12cd34ef56gh78ij90kl12mn34op
```

To get `your/path/to/gbt_automl.yaml`, clone (`git clone https://github.com/gradient-ai/Gradient-Boosted-Trees-and-AutoML`) or download the repo to where you are running the command line.

The results can be viewed by navigating to the Workflows tab within the project where you created your Workflow.

## To run the model deployment, from the command line (advanced)

This deploys the model after model training from running the Notebook or Workflow.

 - Create notebook in a similar manner to the *To run the Notebook* section above, using a C5 or C7 machine, or use the one created from running that section
 - Open the Jupyter notebook interface by clicking the Jupyter symbol on the left-hand navigation bar
 - Open a terminal under the `New` dropdown menu and choosing `Terminal`
 - Enter command `bash`
 - Since when the notebook was created, this repo was cloned, you do not need to clone it again [1]
 - Enter command `apt-get update`
 - Install Maven: `apt install maven` (this also installs Java; answer yes to `Do you want to continue? [Y/n]` by pressing Enter)
 - You can see the versions with `mvn --version`, `java -version`, or other usual diagnostics like `which java` [2]
 - Go to the Java directory: `cd Command_Line`
 - Build the project: `mvn clean package` (should say `BUILD SUCCESS` close to the end of the output)
 - Run the model: `java -jar target/gbtautomlinfer-1.0-jar-with-dependencies.jar` [3]
 - Open second terminal in another browser tab, in the same way that the first one was opened
 - Type `bash`
 - Send a row of inference data to the model [4]: `curl -X GET 'http://localhost:8080/predict?age=39&workclass=State-gov&fnlwgt=77516&education=Bachelors&education-num=13&marital-status=Never-married&occupation=Adm-clerical&relationship=Not-in-family&race=White&sex=Male&capital-gain=2174&capital-loss=0&hours-per-week=40'`

If all goes well the `java -jar` step will show output that looks like

![Deployed model](https://github.com/gradient-ai/Gradient-Boosted-Trees-and-AutoML/blob/main/Command_Line/deployed_model_listening.png)

and the `curl` step will return a prediction from the model for the inference data row

![Model response](https://github.com/gradient-ai/Gradient-Boosted-Trees-and-AutoML/blob/main/Command_Line/deployed_model_response.png)

You can exit the deployed model in the first terminal with `Ctrl+C`, and then from the terminals and shut down the Notebook in the usual Gradient way.

*Notes*

[1] If you did clone the repo again, the command would be `git clone https://github.com/gradient-ai/Gradient-Boosted-Trees-and-AutoML`, but would push the directories down one level to be in `Gradient-Boosted-Trees-and-AutoML/`. You would then have to edit the `App.java` file, or move the files back one level up.

[2] The project was tested with Apache Maven 3.6.0 and Java 11.0.11

[3] Users may note that we are deploying the model already present in the repo, rather than the one output by the user's running of the Notebook or Workflow steps. But the one supplied was created in the same way, so they are all instances of training the same model on the same data. Supplying a trained model keeps the Notebook, Workflow, and command line sections of the project independently runnable, without affecting the results.

[4] This row of inference data is in fact the first row of `income.csv`, already used in the training, but it shows the main point that the setup accepts inference data and the deployed model returns predictions. The inference data sent does not include the ground truth column, `yearly-income`, consistent with this being inference on new data.

### Details of the model deployment setup

The setup is based upon the example in this [blog entry](https://medium.com/spikelab/building-a-machine-learning-application-using-h2o-ai-67ce3681df9c
) by Spikelab, with several modifications for both Gradient and our particular analysis.

The Notebook and/or Workflow project sections output an H2O model in MOJO format, named like

`StackedEnsemble_AllModels_AutoML_20210621_203855.zip`

There is a single associated Java dependency, `h2o-genmodel.jar`.

We use the simple web framework Javalin to build a Java project with Maven that contains the model (`Model.java`) and an app (`App.java`) that sets the model up as a REST endpoint. This is generic to many compute environments, including Gradient.

The project was created on the command line within the Gradient container, as opposed to using an IDE such as IntelliJ or Eclipse. We also use a slightly newer version of H2O, 3.32.1.3.

The Java code is modified from the regression example online to our binary classification example, hence changing the called classes and datatypes, and the H2O artifactID from `h2o-genmodel` to `h2o-genmodel-ext-xgboost` to accomodate the XGBoost models. The inference data contains both categorical and numerical datatypes.

## Conclusions and next steps

This project shows how to run state-of-the-art ML on Gradient outside of deep learning, using Notebooks, Workflows, and a working example of model deployment for the well-known open source library H2O. This opens up a large number of projects via analogy to the examples here.

## Links & credits

 - [Blog entry](https://blog.paperspace.com/gradient-boosted-trees-automl-h2o) for this project
 - [H2O AutoML](https://docs.h2o.ai/h2o/latest-stable/h2o-docs/automl.html)
 - [H2O Java deployment example](https://medium.com/spikelab/building-a-machine-learning-application-using-h2o-ai-67ce3681df9c) by Matias Aravena Gamboa at spikelab, on which ours is based
 - [Javalin](https://javalin.io)
 - [Paperspace Gradient](https://gradient.paperspace.com) 
 - [UCI income dataset](https://archive.ics.uci.edu/ml/datasets/census+income), which we use slightly modified to save on data preparation. The UCI Machine Learning Repository is described further by *Dua, D. and Graff, C. (2019). [UCI Machine Learning Repository](http://archive.ics.uci.edu/ml). Irvine, CA: University of California, School of Information and Computer Science.*

Thanks to Tom Sanfilippo at Paperspace for help with the Java, and to David Banys at Paperspace for help setting up this project as an ML Showcase.
