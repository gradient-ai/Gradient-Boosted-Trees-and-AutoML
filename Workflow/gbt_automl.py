# Run H2O AutoML
#
# Part of the project Gradient Boosted Trees and AutoML at https://github.com/gradient-ai/Gradient-Boosted-Trees-and-AutoML
# Runs same analysis as the Notebook automl_in_h2o.ipynb, but abbreviated code and minimal text
# A model is output that can be deployed, the same as from the Notebook
# Some print statements are added so outputs that went to cells in the Notebook can be seen in the Workflow log
# This script is called by the Workflow YAML automl.yaml
#
# Last updated: Jul 21st 2021

# Setup

import jdk
import os
import subprocess

jdk.install('11', jre=True)

os.environ['PATH'] = "/root/.jre/jdk-11.0.11+9-jre/bin:/usr/local/nvidia/bin:/usr/local/cuda/bin:/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin"
newpath = subprocess.run('echo $PATH', shell=True, check=True, stdout=subprocess.PIPE, universal_newlines=True)
print(newpath)

import h2o
from h2o.automl import H2OAutoML
h2o.init()

# Prepare data

df = h2o.import_file(path = "income.csv")
print(df)
print(df.summary())

y = "yearly-income"
x = df.columns
del x[14]
print(x)

train, valid, test = df.split_frame(
    ratios = [0.6,0.2],
    seed = 123456,
    destination_frames=['train.hex','valid.hex','test.hex']
)

# Train the model using AutoML

aml = H2OAutoML(max_models=20, seed=1)
aml.train(x=x, y=y, training_frame=train)

lb = h2o.automl.get_leaderboard(aml, extra_columns = 'ALL')
print(lb.head(rows=lb.nrows))

print(aml.leader)

# Model performance on testing set

model = aml.leader
predictions = model.predict(test)
print(predictions)

performance = model.model_performance(test)
print(performance)

# Save model

modelfile = model.download_mojo(path="/outputs/modelOutputs", get_genmodel_jar=True)
print("Model saved to " + modelfile)

print('Done')
