pAccuracy=0.0000
cAccuracy=0.0000
i=1
path="/home/dipawesh/Dipawesh/HWTTPE-NEW/THW-TwoStep/LibSVM/FirstStep"
#-------------------------------Training -----------------------------------------------------------
	java -classpath $path/libsvm.jar:$path/weka.jar weka.classifiers.meta.FilteredClassifier -t $path/ExpandedTrainingSet/TrainSet.arff -x 10 -c last -i -d $path/Models/SVMx10_iteration$i.model -F 'weka.filters.unsupervised.attribute.StringToWordVector -R 1 -W 3000 -N 0 -stemmer weka.core.stemmers.NullStemmer -T -I -M 1 -tokenizer "weka.core.tokenizers.NGramTokenizer -min 2 -max 3" -C'  -W weka.classifiers.functions.LibSVM -- -B >$path/Performance/SVM_x10_iteration$i.txt
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
		java -cp $path/src/FindInstancesForProbabilisticExpansion.jar FindInstancesForProbabilisticExpansion $path/Predictions/SVM_iteration$i.txt $path/TestSet/SeedSet.txt $path/ExpandedTrainingSet/TrainSet.arff $path/ExpandedTrainingSet/SeedSet.txt 8 $path/ExpansionDetails/ExpansionDetails.txt $i >$path/ExpansionDetails/Expansion$i.txt
        echo 'iteration' $i 'Expansion over';
        i=$(echo "$i+1" | bc -l)
#-------------------------------Training -----------------------------------------------------------
	    java -classpath $path/libsvm.jar:$path/weka.jar weka.classifiers.meta.FilteredClassifier -t $path/ExpandedTrainingSet/TrainSet.arff -x 10 -c last -i -d $path/Models/SVMx10_iteration$i.model -F 'weka.filters.unsupervised.attribute.StringToWordVector -R 1 -W 3000 -N 0 -stemmer weka.core.stemmers.NullStemmer -T -I -M 1 -tokenizer "weka.core.tokenizers.NGramTokenizer -min 2 -max 3" -C'  -W weka.classifiers.functions.LibSVM -- -B >$path/Performance/SVM_x10_iteration$i.txt
	     cAccuracy=$(java -cp $path/src/AccuracyFinder.jar AccuracyFinder $path/Performance/SVM_x10_iteration$i.txt)
	     echo 'iteration' $i 'Training over with accuracy' $cAccuracy
done
echo 'First Step OVER'
i=$(echo "$i-1" | bc -l)
 #------------------------------Obtain prediction on full test set file -------------------------------------------------------------
		java -classpath $path/weka.jar:$path/libsvm.jar weka.classifiers.meta.FilteredClassifier -T $path/FullTestSet/TestSet.arff  -l $path/Models/SVMx10_iteration$i.model -p 0  > $path/Predictions/PredictionOnFullTestSet.txt
echo 'Obtained Predictions with model' $i
#-------------------Remove last line from generated prediction file----------------------------------------------------------------
sed -i '$ d' $path/Predictions/PredictionOnFullTestSet.txt
#--------------------------Assimulate prediction & file to be classified-----------------------------AssimulatePrediction_FileToBeClassified---------------------------
java -cp $path/src/AssimulatePrediction_FileToBeClassified.jar AssimulatePrediction_FileToBeClassified $path/Predictions/PredictionOnFullTestSet.txt  $path/FullTestSet/SeedSet.txt  $path/Result/ClassifiedFile.txt
echo 'Assimulated prediction & file to be classified'
#------------------------------Obtain Prediction On Gold Test Set----------------------------------------------------
java -classpath $path/weka.jar:$path/libsvm.jar weka.classifiers.meta.FilteredClassifier -T $path/GoldStandardTestSet/GoldSet.arff  -l $path/Models/SVMx10_iteration$i.model -p 0  > $path/Predictions/PredictionOnGoldData.txt
echo 'Obtained Prediction On Gold Test Set'
#------------------------------Collect Performance on gold test set--------------------------------------------------------
java -cp $path/src/ObtainPerformanceOnGoldStandardFirstStep.jar ObtainPerformanceOnGoldStandardFirstStep $path/Predictions/PredictionOnGoldData.txt $path/Performance/PerformanceOnGoldData.txt
echo 'Collect Performance on gold test set'
#------------------------------Obtain Prediction On Gold Test Set(EASY)----------------------------------------------------
java -classpath $path/weka.jar:$path/libsvm.jar weka.classifiers.meta.FilteredClassifier -T $path/GoldStandardTestSet/EasyCases/GoldTestSetFirstStep\(EASY\).arff  -l $path/Models/SVMx10_iteration$i.model -p 0  > $path/Predictions/PredictionOnGoldData\(EASY\).txt
echo 'Obtained Prediction On Gold Test Set(EASY)'
#------------------------------Collect Performance on gold test set(EASY)--------------------------------------------------------
java -cp $path/src/ObtainPerformanceOnGoldStandardFirstStep.jar ObtainPerformanceOnGoldStandardFirstStep $path/Predictions/PredictionOnGoldData\(EASY\).txt $path/Performance/PerformanceOnGoldData\(EASY\).txt
#-----------------------------Extract Temporal instances from result of first step-----------------------------------------------
java -cp $path/src/ExtractTemporalInstancesFromResultOfFirstStep.jar ExtractTemporalInstancesFromResultOfFirstStep $path/Result/ClassifiedFile.txt /home/dipawesh/Dipawesh/HWTTPE-NEW/THW-TwoStep/LibSVM/SecondStep/TestSet/ /home/dipawesh/Dipawesh/HWTTPE-NEW/THW-TwoStep/LibSVM/SecondStep/TestSet/
echo 'Extracted Temporal instances from result of first step'
#~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
#~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
#------------------------------------Second Step-----------------------------------------------------------------------------------
pAccuracy=0.0000
cAccuracy=0.0000
i=1
path="/home/dipawesh/Dipawesh/HWTTPE-NEW/THW-TwoStep/LibSVM/SecondStep"
#-------------------------------Training -----------------------------------------------------------
	java -classpath $path/libsvm.jar:$path/weka.jar weka.classifiers.meta.FilteredClassifier -t $path/ExpandedTrainingSet/TrainSet.arff -x 10 -c last -i -d $path/Models/SVMx10_iteration$i.model -F 'weka.filters.unsupervised.attribute.StringToWordVector -R 1 -W 3000 -N 0 -stemmer weka.core.stemmers.NullStemmer -T -I -M 1 -tokenizer "weka.core.tokenizers.NGramTokenizer -min 2 -max 3" -C'  -W weka.classifiers.functions.LibSVM -- -B >$path/Performance/SVM_x10_iteration$i.txt
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
		java -cp $path/src/FindInstancesForProbabilisticExpansion.jar FindInstancesForProbabilisticExpansion $path/Predictions/SVM_iteration$i.txt $path/TestSet/SeedSet.txt $path/ExpandedTrainingSet/TrainSet.arff $path/ExpandedTrainingSet/SeedSet.txt 8 $path/ExpansionDetails/ExpansionDetails.txt $i >$path/ExpansionDetails/Expansion$i.txt
        echo 'iteration' $i 'Expansion over';
        i=$(echo "$i+1" | bc -l)
#-------------------------------Training -----------------------------------------------------------
	    java -classpath $path/libsvm.jar:$path/weka.jar weka.classifiers.meta.FilteredClassifier -t $path/ExpandedTrainingSet/TrainSet.arff -x 10 -c last -i -d $path/Models/SVMx10_iteration$i.model -F 'weka.filters.unsupervised.attribute.StringToWordVector -R 1 -W 3000 -N 0 -stemmer weka.core.stemmers.NullStemmer -T -I -M 1 -tokenizer "weka.core.tokenizers.NGramTokenizer -min 2 -max 3" -C'  -W weka.classifiers.functions.LibSVM -- -B >$path/Performance/SVM_x10_iteration$i.txt
	     echo 'iteration' $i 'Training over'
	     cAccuracy=$(java -cp $path/src/AccuracyFinder.jar AccuracyFinder $path/Performance/SVM_x10_iteration$i.txt)
done
echo 'Second Step OVER'
i=$(echo "$i-1" | bc -l)
 #------------------------------Obtain prediction on test set -------------------------------------------------------------
		java -classpath $path/weka.jar:$path/libsvm.jar weka.classifiers.meta.FilteredClassifier -T $path/TestSet/TestSet.arff  -l $path/Models/SVMx10_iteration$i.model -p 0  > $path/Predictions/PredictionOnFullTestSet.txt
echo 'Obtained Predictions with model' $i
#-------------------Remove last line from generated prediction file----------------------------------------------------------------
sed -i '$ d' $path/Predictions/PredictionOnFullTestSet.txt
#--------------------------Assimulate prediction & file to be classified--------------------------------------------------------
java -cp $path/src/AssimulatePrediction_FileToBeClassified.jar AssimulatePrediction_FileToBeClassified $path/Predictions/PredictionOnFullTestSet.txt  $path/TestSet/SeedSet.txt  $path/Result/ClassifiedFile.txt
echo 'Assimulated prediction & file to be classified'
#------------------------------Obtain Prediction On Gold Test Set----------------------------------------------------
java -classpath $path/weka.jar:$path/libsvm.jar weka.classifiers.meta.FilteredClassifier -T $path/GoldStandardTestSet/GoldSet.arff  -l $path/Models/SVMx10_iteration$i.model -p 0  > $path/Predictions/PredictionOnGoldData.txt
echo 'Obtained Prediction On Gold Test Set'
#------------------------------Collect Performance on gold test set--------------------------------------------------------
java -cp $path/src/ObtainPerformanceOnGoldStandardSecondStep.jar ObtainPerformanceOnGoldStandardSecondStep $path/Predictions/PredictionOnGoldData.txt $path/Performance/PerformanceOnGoldData.txt
echo 'Collect Performance on gold test set'
#------------------------------Obtain Prediction On Gold Test Set(EASY)----------------------------------------------------
java -classpath $path/weka.jar:$path/libsvm.jar weka.classifiers.meta.FilteredClassifier -T $path/GoldStandardTestSet/EasyCases/GoldTestSetSecondStep\(EASY\).arff  -l $path/Models/SVMx10_iteration$i.model -p 0  > $path/Predictions/PredictionOnGoldData\(EASY\).txt
echo 'Obtained Prediction On Gold Test Set(EASY)'
#------------------------------Collect Performance on gold test set(EASY)--------------------------------------------------------
java -cp $path/src/ObtainPerformanceOnGoldStandardSecondStep.jar ObtainPerformanceOnGoldStandardSecondStep $path/Predictions/PredictionOnGoldData\(EASY\).txt $path/Performance/PerformanceOnGoldData\(EASY\).txt
echo 'Collect Performance on gold test set(EASY)'
#---------------------------------Merge result from first & second Step(Final THW)---------------------------------------------------
java -cp /home/dipawesh/Dipawesh/HWTTPE-NEW/THW-TwoStep/LibSVM/FirstStep/src/EATIFFSR.jar EATIFFSR /home/dipawesh/Dipawesh/HWTTPE-NEW/THW-TwoStep/LibSVM/FirstStep/Result/ClassifiedFile.txt /home/dipawesh/Dipawesh/HWTTPE-NEW/THW-TwoStep/LibSVM/SecondStep/Result/ClassifiedFile.txt /home/dipawesh/Dipawesh/HWTTPE-NEW/THW-TwoStep/LibSVM/THW.txt
echo 'result from first & second Step(Final THW) are merged'
#---------------------------------Sentence Classification v1---------------------------------------------------------------------
#java -cp $path/src/SentenceClassificationv1.jar SentenceClassificationv1 $path/GoldStandardTestSet/SensetaggedFileForSentenceClassification.txt /home/dipawesh/LibSVM/THW.txt $path/Performance/SentenceClassificationEvaluationDetails\(v1\).txt $path/Performance/PerformanceOfSentenceClassificationV1.txt
#echo 'Done with Sentence Classification v1'
#--------------------------------produce evaluation file Sentence Classification v2-----------------------------
#java -cp $path/src/SentenceClassificationReplacingTemporalWordsWithGlossesv2.jar SentenceClassificationReplacingTemporalWordsWithGlossesv2 $path/GoldStandardTestSet/SensetaggedFileForSentenceClassification.txt /home/dipawesh/LibSVM/THW.txt $path/GoldStandardTestSet/SentenseClassificationV2EvaluationFile.arff
#echo 'Evaluation file for sentence classification v2 produced'
#----------------------------------Obtain prediction on evaluation file Sentence Classification v2--------------------------
#java -classpath $path/weka.jar:$path/libsvm.jar weka.classifiers.meta.FilteredClassifier -T $path/GoldStandardTestSet/SentenseClassificationV2EvaluationFile.arff  -l $path/Models/SVMx10_iteration$i.model -p 0  > $path/Predictions/PredictionSentenceClassificationV2.txt
#echo 'Predictions on evaluation files obtained'
#----------------------------------Obtain performance on prediction obtained---------------------------------------
#java -cp $path/src/ObtainPerformanceOnGoldStandardSecondStep.jar ObtainPerformanceOnGoldStandardSecondStep $path/Predictions/PredictionSentenceClassificationV2.txt $path/Performance/PerformanceOnSentenceClassificationV2.txt
#echo 'performance of sentence classification v2 obtained'
#~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
#~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
