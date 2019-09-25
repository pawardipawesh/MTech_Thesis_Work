# MTech_Thesis_Work

This needs following jar files
Weka.jar
LibSVM.jar


Temporal resource for Hindi Language

Directory structure of project
TIWC-OneStep-- Tempo-Indo wordnet with one step classification approach.

TIWC-TwoStep-- Tempo-Indo wordnet with two step classification approach

TIWC-TwoStep/FirstStep-- Consists of files generated or used in first step of two step classification approach. Subfolders are explained in subfolder section.

TIWC-TwoStep/SecondStep-- Consists of files generated or used in second step of two step classification approach. Subfolders are explained in subfolder section.

TIWC-OneStep-- Consists of files generated or used in one step classification approach. Subfolders are explained in subfolder section.

##Subfolders Each of TIWC-TwoStep/FirstStep, TIWC-TwoStep/SecondStep, TIWC-OneStep consists of following folders-

performance- consists of performance files geneirated after training in each iteration.

predictions- consists of predictions generated after testing in each iteration.Files are named as "SVM_iteration{iteration number}time{cycle number}". It is also consisting of files "PredictionsOnGoldStandardTestset" and "PredictionsOnFileToBeClassified". Prior consist of predcition on gold standard test set used in first step of two step classification approach and later consists of predictions obtained on file consisting of words retrieved from entire wordnet.

ExpandedTrainingSet- Consists of final expanded TrainingSet & seed word set files.

ExpansionDetails - consists of expansion files generated during expansion process. Files are named as Expansion{iteration number}time{cycle number}. These files are consisting of number of instances of each class that are added in an iteration of corresponding cycle. It also consist of ExpansionDetails file which consists ofinformation about instance added in expanded training set such as Instance number, prediction Value, Tense and iteartion number.

GoldStandardTestSet-- Consists of files GoldTestSet.aroff and GoldTestSet.txt. Prior consists of glosses & is used for testing. Later consists of seed words corresponding to glosses in GoldTestSet.arff

Models - consists of model genearted in each iteration of evry cycle.These are named as SVMx{n-fold Cross validation }_iteration{iteration number}time{cycle number}.

Result - Consists of synsets classified among desired classes of that step.

src - consists of java programs used in project

TrainingSet - consists of initial training set used in that step.

TestSet - consists of test set files obtained after dividing synsets of wordnet in n folds.n is 10 for first step two step classification approach, 8 for 2nd step of two step classification approach and 10 for One Step classification approach.
