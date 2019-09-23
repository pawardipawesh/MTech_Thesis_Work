#!/bin/sh
pAccuracy=0.0000
cAccuracy=1.0000
j=0
time="time"
for (( ; $(echo "$pAccuracy < $cAccuracy" | bc -l); ))
do
 time="time"
 j=$(echo "$j+1"|bc)
 time=$time$j
 for (( i=1; i <= 11; i++ ))
 do
	#-------------------------------Training -----------------------------------------------------------
	java -classpath /home/stud/mtech/subrata.mtcs14/Hindi_wordnet_time_tagging_OneStep/libsvm.jar:/home/stud/mtech/subrata.mtcs14/Hindi_wordnet_time_tagging_OneStep/weka.jar weka.classifiers.meta.FilteredClassifier -t /home/stud/mtech/subrata.mtcs14/Hindi_wordnet_time_tagging_OneStep/ExpandedTrainingSet/TrainingSet.arff -x 10 -c last -i -d /home/stud/mtech/subrata.mtcs14/Hindi_wordnet_time_tagging_OneStep/Models/SVMx10_iteration$i$time.model -F 'weka.filters.unsupervised.attribute.StringToWordVector -R 1 -W 3000 -N 0 -stemmer weka.core.stemmers.NullStemmer -T -I -M 1 -tokenizer weka.core.tokenizers.WordTokenizer -C'  -W weka.classifiers.functions.LibSVM -- -B >/home/stud/mtech/subrata.mtcs14/Hindi_wordnet_time_tagging_OneStep/Performance/SVM_x10_iteration$i$time.txt
        if [ $i == 1 ] 
	then 
	pAccuracy=$(java -cp /home/stud/mtech/subrata.mtcs14/Hindi_wordnet_time_tagging_OneStep/src/AccuracyFinder.jar AccuracyFinder /home/stud/mtech/subrata.mtcs14/Hindi_wordnet_time_tagging_OneStep/Performance/SVM_x10_iteration$i$time.txt)
        fi
	echo 'iteration' $i$time 'Training over'
	if [ $i == 11 ]
        then
              
         	cAccuracy=$(java -cp /home/stud/mtech/subrata.mtcs14/Hindi_wordnet_time_tagging_OneStep/src/AccuracyFinder.jar AccuracyFinder /home/stud/mtech/subrata.mtcs14/Hindi_wordnet_time_tagging_OneStep/Performance/SVM_x10_iteration$i$time.txt)
        else
  
        
		#------------------------------Training  Over-------------------------------------------------------------------
		#-------------------------------Testing -----------------------------------------------------------------------
		java -classpath /home/stud/mtech/subrata.mtcs14/Hindi_wordnet_time_tagging_OneStep/weka.jar:/home/stud/mtech/subrata.mtcs14/Hindi_wordnet_time_tagging_OneStep/libsvm.jar weka.classifiers.meta.FilteredClassifier -T /home/stud/mtech/subrata.mtcs14/Hindi_wordnet_time_tagging_OneStep/TestSet/TestSetInChunks/TestSet$i.arff  -l /home/stud/mtech/subrata.mtcs14/Hindi_wordnet_time_tagging_OneStep/Models/SVMx10_iteration$i$time.model -p 0  > /home/stud/mtech/subrata.mtcs14/Hindi_wordnet_time_tagging_OneStep/Predictions/SVM_iteration$i$time.txt
		echo 'iteration' $i$time 'Testing over';
		#-------------------------------Testing over-------------------------------------------------------------------------------
		#-------------------------------------------------------Expansion of training set------------------
		java -cp /home/stud/mtech/subrata.mtcs14/Hindi_wordnet_time_tagging_OneStep/src/FindInstancesForProbabilisticExpansion.jar FindInstancesForProbabilisticExpansion /home/stud/mtech/subrata.mtcs14/Hindi_wordnet_time_tagging_OneStep/Predictions/SVM_iteration$i$time.txt /home/stud/mtech/subrata.mtcs14/Hindi_wordnet_time_tagging_OneStep/TestSet/TestSetInChunks/dataset$i.txt /home/stud/mtech/subrata.mtcs14/Hindi_wordnet_time_tagging_OneStep/ExpandedTrainingSet/TrainingSet.arff /home/stud/mtech/subrata.mtcs14/Hindi_wordnet_time_tagging_OneStep/ExpandedTrainingSet/TrainingSeedWords.txt 8 /home/stud/mtech/subrata.mtcs14/Hindi_wordnet_time_tagging_OneStep/ExpansionDetails/ExpansionDetails.txt $i$time >/home/stud/mtech/subrata.mtcs14/Hindi_wordnet_time_tagging_OneStep/ExpansionDetails/Expansion$i$time.txt
		echo 'iteration' $i$time 'Expansion over';
        fi
  done
      		echo 'iteration1 Accuracy=' $pAccuracy 'iteration11 Accuracy=' $cAccuracy

done
echo 'PROCESS OVER'
