pAccuracy=0.0000
cAccuracy=0.0000
i=1
path="/home/dipawesh/THW-TwoStep/LibSVM/FirstStep"
#-------------------------------Training -----------------------------------------------------------
	java -classpath $path/libsvm.jar:$path/weka.jar weka.classifiers.meta.FilteredClassifier -t $path/ExpandedTrainingSet/TrainSetWE.arff -x 10 -i  -c 202 -d $path/Models/SVMx10_iteration$i.model -F 'weka.filters.unsupervised.attribute.StringToWordVector -R first -W 1000 -N 0 -stemmer weka.core.stemmers.NullStemmer -T -I -M 1 -tokenizer weka.core.tokenizers.WordTokenizer -C'    -W weka.classifiers.functions.SMO -- -C 2 -L 0.001 -P 1.0E-12 -N 0 -V -1 -W 1 -K "weka.classifiers.functions.supportVector.PolyKernel -C 250007 -E 1.0" -M >$path/Performance/SVM_x10_iteration$i.txt
	echo 'iteration' $i 'Training over'
	pAccuracy=$(java -cp $path/src/AccuracyFinder.jar AccuracyFinder $path/Performance/SVM_x10_iteration$i.txt)
cAccuracy=$(echo "$pAccuracy+0.0001" | bc -l)	
for (( ; $(echo "$pAccuracy < $cAccuracy" | bc -l); ))
do
        pAccuracy=$cAccuracy
 #-------------------------------Testing -----------------------------------------------------------------------
		java -classpath $path/weka.jar:$path/libsvm.jar weka.classifiers.meta.FilteredClassifier -T $path/TestSet/TestSetWE.arff  -l $path/Models/SVMx10_iteration$i.model -p 0  > $path/Predictions/SVM_iteration$i.txt
        echo 'iteration' $i 'Testing over';
#-------------------------------------------------------Expansion of training set------------------
		java -cp $path/src/FindInstancesForWEbasedPEFirstStep.jar FindInstancesForWEbasedPEFirstStep $path/TestSet/TestSetWE.arff $path/TestSet/SeedSet.txt $path/Predictions/SVM_iteration$i.txt $path/ExpandedTrainingSet/TrainSetWE.arff $path/ExpandedTrainingSet/SeedSet.txt $path/ExpansionDetails/ExpansionDetails.txt $i
        echo 'iteration' $i 'Expansion over';
        i=$(echo "$i+1" | bc -l)
#-------------------------------Training -----------------------------------------------------------
	    java -classpath $path/libsvm.jar:$path/weka.jar weka.classifiers.meta.FilteredClassifier -t $path/ExpandedTrainingSet/TrainSetWE.arff -x 10 -i  -c 202 -d $path/Models/SVMx10_iteration$i.model -F 'weka.filters.unsupervised.attribute.StringToWordVector -R first -W 1000 -N 0 -stemmer weka.core.stemmers.NullStemmer -T -I -M 1 -tokenizer weka.core.tokenizers.WordTokenizer -C'    -W weka.classifiers.functions.SMO -- -C 2 -L 0.001 -P 1.0E-12 -N 0 -V -1 -W 1 -K "weka.classifiers.functions.supportVector.PolyKernel -C 250007 -E 1.0" -M >$path/Performance/SVM_x10_iteration$i.txt
	     echo 'iteration' $i 'Training over'
	     cAccuracy=$(java -cp $path/src/AccuracyFinder.jar AccuracyFinder $path/Performance/SVM_x10_iteration$i.txt)
done
echo 'First Step OVER'
i=$(echo "$i-1" | bc -l)
 #------------------------------Obtain prediction on full test set file -------------------------------------------------------------
		java -classpath $path/weka.jar:$path/libsvm.jar weka.classifiers.meta.FilteredClassifier -T $path/FullTestSet/TestSetWE.arff  -l $path/Models/SVMx10_iteration$i.model -p 0  > $path/Predictions/PredictionOnFullTestSet.txt
echo 'Obtained Predictions with model' $i
#-------------------Remove last line from generated prediction file----------------------------------------------------------------
sed -i '$ d' $path/Predictions/PredictionOnFullTestSet.txt
#--------------------------Assimulate prediction & file to be classified--------------------------------------------------------
java -cp $path/src/AssimulatePrediction_FileToBeClassified.jar AssimulatePrediction_FileToBeClassified $path/Predictions/PredictionOnFullTestSet.txt  $path/FullTestSet/SeedSet.txt  $path/Result/ClassifiedFile.txt
echo 'Assimulated prediction & file to be classified'
#------------------------------Obtain Prediction On Gold Test Set----------------------------------------------------
java -classpath $path/weka.jar:$path/libsvm.jar weka.classifiers.meta.FilteredClassifier -T $path/GoldStandardTestSet/GoldSetWE.arff  -l $path/Models/SVMx10_iteration$i.model -p 0  > $path/Predictions/PredictionOnGoldData.txt
echo 'Obtained Prediction On Gold Test Set'
#------------------------------Obtain Prediction On Gold Test Set(EASY)----------------------------------------------------
java -classpath $path/weka.jar:$path/libsvm.jar weka.classifiers.meta.FilteredClassifier -T $path/GoldStandardTestSet/EasyCases/GoldSetWE.arff  -l $path/Models/SVMx10_iteration$i.model -p 0  > $path/Predictions/PredictionOnGoldData\(EASY\).txt
echo 'Obtained Prediction On Gold Test Set(EASY)'
#-----------------------------Extract Temporal instances from result of first step-----------------------------------------------
java -cp $path/src/ExtractTemporalInstancesFromResultOfFirstStep.jar ExtractTemporalInstancesFromResultOfFirstStep $path/Result/ClassifiedFile.txt /home/dipawesh/THW-TwoStep/LibSVM/SecondStep/TestSet/ /home/dipawesh/THW-TwoStep/LibSVM/SecondStep/TestSet/  /home/dipawesh/THW-TwoStep/LibSVM/SecondStep/TrainingSet/TrainSetWE.arff $path/FullTestSet/TestSetWE.arff
echo 'Extracted Temporal instances from result of first step'
scp -r /home/dipawesh/THW-TwoStep/LibSVM/SecondStep/TestSet/* /home/dipawesh/THW-TwoStep/LibSVM/SecondStep/FullTestSet/
echo 'Test set files are copied to full test set'
#~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
#~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
#------------------------------------Second Step-----------------------------------------------------------------------------------
pAccuracy=0.0000
cAccuracy=0.0000
i=1
path="/home/dipawesh/THW-TwoStep/LibSVM/SecondStep"
#-------------------------------Training -----------------------------------------------------------
	java -classpath $path/libsvm.jar:$path/weka.jar weka.classifiers.meta.FilteredClassifier -t $path/ExpandedTrainingSet/TrainSetWE.arff -x 10 -i  -c 202 -d $path/Models/SVMx10_iteration$i.model -F 'weka.filters.unsupervised.attribute.StringToWordVector -R first -W 1000 -N 0 -stemmer weka.core.stemmers.NullStemmer -T -I -M 1 -tokenizer weka.core.tokenizers.WordTokenizer -C'    -W weka.classifiers.functions.SMO -- -C 2 -L 0.001 -P 1.0E-12 -N 0 -V -1 -W 1 -K "weka.classifiers.functions.supportVector.PolyKernel -C 250007 -E 1.0" -M >$path/Performance/SVM_x10_iteration$i.txt
	echo 'iteration' $i 'Training over'
	pAccuracy=$(java -cp $path/src/AccuracyFinder.jar AccuracyFinder $path/Performance/SVM_x10_iteration$i.txt)
cAccuracy=$(echo "$pAccuracy+0.0001" | bc -l)	
for (( ; $(echo "$pAccuracy < $cAccuracy" | bc -l); ))
do
        pAccuracy=$cAccuracy
 #-------------------------------Testing -----------------------------------------------------------------------
		java -classpath $path/weka.jar:$path/libsvm.jar weka.classifiers.meta.FilteredClassifier -T $path/TestSet/TestSet.arff  -l $path/Models/SVMx10_iteration$i.model -p 0  > $path/Predictions/SVM_iteration$i.txt
        echo 'iteration' $i 'Testing over';
#-------------------------------------------------------Expansion of training set------------------
		java -cp $path/src/FindInstancesForWEbasedPESecondStep.jar FindInstancesForWEbasedPESecondStep $path/TestSet/TestSet.arff $path/TestSet/SeedSet.txt $path/Predictions/SVM_iteration$i.txt $path/ExpandedTrainingSet/TrainSetWE.arff $path/ExpandedTrainingSet/SeedSet.txt $path/ExpansionDetails/ExpansionDetails.txt $i
        echo 'iteration' $i 'Expansion over';
        i=$(echo "$i+1" | bc -l)
#-------------------------------Training -----------------------------------------------------------
	    java -classpath $path/libsvm.jar:$path/weka.jar weka.classifiers.meta.FilteredClassifier -t $path/ExpandedTrainingSet/TrainSetWE.arff -x 10 -i  -c 202 -d $path/Models/SVMx10_iteration$i.model -F 'weka.filters.unsupervised.attribute.StringToWordVector -R first -W 1000 -N 0 -stemmer weka.core.stemmers.NullStemmer -T -I -M 1 -tokenizer weka.core.tokenizers.WordTokenizer -C'    -W weka.classifiers.functions.SMO -- -C 2 -L 0.001 -P 1.0E-12 -N 0 -V -1 -W 1 -K "weka.classifiers.functions.supportVector.PolyKernel -C 250007 -E 1.0" -M >$path/Performance/SVM_x10_iteration$i.txt
	     echo 'iteration' $i 'Training over'
	     cAccuracy=$(java -cp $path/src/AccuracyFinder.jar AccuracyFinder $path/Performance/SVM_x10_iteration$i.txt)
done
echo 'Second Step OVER'
i=$(echo "$i-1" | bc -l)
 #------------------------------Obtain prediction on Full test set -------------------------------------------------------------
		java -classpath $path/weka.jar:$path/libsvm.jar weka.classifiers.meta.FilteredClassifier -T $path/FullTestSet/TestSet.arff  -l $path/Models/SVMx10_iteration$i.model -p 0  > $path/Predictions/PredictionOnFullTestSet.txt
echo 'Obtained Predictions with model' $i
#-------------------Remove last line from generated prediction file----------------------------------------------------------------
sed -i '$ d' $path/Predictions/PredictionOnFullTestSet.txt
#--------------------------Assimulate prediction & file to be classified--------------------------------------------------------
java -cp $path/src/AssimulatePrediction_FileToBeClassified.jar AssimulatePrediction_FileToBeClassified $path/Predictions/PredictionOnFullTestSet.txt  $path/FullTestSet/SeedSet.txt  $path/Result/ClassifiedFile.txt
echo 'Assimulated prediction & file to be classified'
#------------------------------Obtain Prediction On Gold Test Set----------------------------------------------------
java -classpath $path/weka.jar:$path/libsvm.jar weka.classifiers.meta.FilteredClassifier -T $path/GoldStandardTestSet/GoldSetWE.arff  -l $path/Models/SVMx10_iteration$i.model -p 0  > $path/Predictions/PredictionOnGoldData.txt
echo 'Obtained Prediction On Gold Test Set'
#------------------------------Obtain Prediction On Gold Test Set(EASY)----------------------------------------------------
java -classpath $path/weka.jar:$path/libsvm.jar weka.classifiers.meta.FilteredClassifier -T $path/GoldStandardTestSet/EasyCases/GoldSetWE.arff  -l $path/Models/SVMx10_iteration$i.model -p 0  > $path/Predictions/PredictionOnGoldData\(EASY\).txt
echo 'Obtained Prediction On Gold Test Set(EASY)'
#---------------------------------Merge result from first & second Step(Final THW)---------------------------------------------------
java -cp $path/src/MergeResultsOfFirstAndSecondStep.jar MergeResultsOfFirstAndSecondStep /home/dipawesh/THW-TwoStep/LibSVM/FirstStep/Result/ClassifiedFile.txt $path/Result/ClassifiedFile.txt /home/dipawesh/THW-TwoStep/LibSVM/THW.txt
echo 'result from first & second Step(Final THW) are merged'
#---------------------------------Sentence Classification v1---------------------------------------------------------------------
#java -cp $path/src/SentenceClassificationv1.jar SentenceClassificationv1 $path/GoldStandardTestSet/SensetaggedFileForSentenceClassification.txt /home/dipawesh/Dipawesh/HWTTPE-NEW/THW-TwoStep/J48/THW.txt $path/Performance/SentenceClassificationEvaluationDetails\(v1\).txt $path/Performance/PerformanceOfSentenceClassificationV1.txt
#echo 'Done with Sentence Classification v1'
#--------------------------------produce evaluation file Sentence Classification v2-----------------------------
#java -cp $path/src/SentenceClassificationReplacingTemporalWordsWithGlossesv2.jar SentenceClassificationReplacingTemporalWordsWithGlossesv2 $path/GoldStandardTestSet/SensetaggedFileForSentenceClassification.txt /home/dipawesh/Dipawesh/HWTTPE-NEW/THW-TwoStep/J48/THW.txt $path/GoldStandardTestSet/SentenseClassificationV2EvaluationFile.arff
#echo 'Evaluation file for sentence classification v2 produced'
#----------------------------------Obtain prediction on evaluation file Sentence Classification v2--------------------------
#java -classpath $path/weka.jar:$path/libsvm.jar weka.classifiers.meta.FilteredClassifier -T $path/GoldStandardTestSet/SentenseClassificationV2EvaluationFile.arff  -l $path/Models/SVMx10_iteration$i.model -p 0  > $path/Predictions/PredictionSentenceClassificationV2.txt
#echo 'Predictions on evaluation files obtained'
#----------------------------------Obtain performance on prediction obtained---------------------------------------
#java -cp $path/src/ObtainPerformanceOnGoldStandardSecondStep.jar ObtainPerformanceOnGoldStandardSecondStep $path/Predictions/PredictionSentenceClassificationV2.txt $path/Performance/PerformanceOnSentenceClassificationV2.txt
#echo 'performance of sentence classification v2 obtained'
#~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
#~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
