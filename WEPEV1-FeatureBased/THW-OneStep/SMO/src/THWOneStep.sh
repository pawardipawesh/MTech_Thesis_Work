pAccuracy=0.0000
cAccuracy=0.0000
i=1
path="/home/dipawesh/THW-OneStep/LibSVM"
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
		java -cp $path/src/FindInstancesForWEbasedPEOneStep.jar FindInstancesForWEbasedPEOneStep $path/TestSet/TestSetWE.arff $path/TestSet/SeedSet.txt $path/Predictions/SVM_iteration$i.txt $path/ExpandedTrainingSet/TrainSetWE.arff $path/ExpandedTrainingSet/SeedSet.txt $path/ExpansionDetails/ExpansionDetails.txt $i
        echo 'iteration' $i 'Expansion over';
        i=$(echo "$i+1" | bc -l)
#-------------------------------Training -----------------------------------------------------------
	   java -classpath $path/libsvm.jar:$path/weka.jar weka.classifiers.meta.FilteredClassifier -t $path/ExpandedTrainingSet/TrainSetWE.arff -x 10 -i  -c 202 -d $path/Models/SVMx10_iteration$i.model -F 'weka.filters.unsupervised.attribute.StringToWordVector -R first -W 1000 -N 0 -stemmer weka.core.stemmers.NullStemmer -T -I -M 1 -tokenizer weka.core.tokenizers.WordTokenizer -C'    -W weka.classifiers.functions.SMO -- -C 2 -L 0.001 -P 1.0E-12 -N 0 -V -1 -W 1 -K "weka.classifiers.functions.supportVector.PolyKernel -C 250007 -E 1.0" -M >$path/Performance/SVM_x10_iteration$i.txt
	     echo 'iteration' $i 'Training over'
	     cAccuracy=$(java -cp $path/src/AccuracyFinder.jar AccuracyFinder $path/Performance/SVM_x10_iteration$i.txt)
done
echo 'PROCESS OVER'
i=$(echo "$i-1" | bc -l)
 #------------------------------Obtain prediction For Final THW -----------------------------------------------------------------------
		java -classpath $path/weka.jar:$path/libsvm.jar weka.classifiers.meta.FilteredClassifier -T $path/FullTestSet/TestSetWE.arff  -l $path/Models/SVMx10_iteration$i.model -p 0  > $path/Predictions/PredictionForFinalTHW.txt
echo 'Obtained Predictions with model' $i
#-------------------Remove last line from generated prediction file----------------------------------------------------------------
sed -i '$ d' $path/Predictions/PredictionForFinalTHW.txt
#--------------------------Assimulate prediction & file to be classified--------------------------------------------------------
java -cp $path/src/AssimulatePrediction_FileToBeClassified.jar AssimulatePrediction_FileToBeClassified $path/Predictions/PredictionForFinalTHW.txt  $path/FullTestSet/SeedSet.txt  $path/Result/ClassifiedFile.txt
#------------------------------Obtain Prediction On Gold Test Set----------------------------------------------------
java -classpath $path/weka.jar:$path/libsvm.jar weka.classifiers.meta.FilteredClassifier -T $path/GoldStandardTestSet/GoldSetWE.arff  -l $path/Models/SVMx10_iteration$i.model -p 0  > $path/Predictions/PredictionOnGoldData.txt
#------------------------------Obtain Prediction On Gold Test Set(EASY CASES)----------------------------------------------------
java -classpath $path/weka.jar:$path/libsvm.jar weka.classifiers.meta.FilteredClassifier -T $path/GoldStandardTestSet/EasyCases/GoldSetWE.arff  -l $path/Models/SVMx10_iteration$i.model -p 0  > $path/Predictions/PredictionOnGoldData\(EASY\).txt
echo 'Entire process done'
