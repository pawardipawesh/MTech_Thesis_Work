#!/bin/bash
getFMeasure() {
		# String2WordVector
		# ================
		java weka.filters.unsupervised.attribute.StringToWordVector -b -i /home/shweta.pcs13/gen02/trainingB_$1_$2.arff -o /home/shweta.pcs13/gen02/trainingB_$1_$2_s2w.arff -r /home/shweta.pcs13/gen02/testB_$1_$2.arff -s /home/shweta.pcs13/gen02/testB_$1_$2_s2w.arff
		# Reorder
		# =======
		java weka.filters.unsupervised.attribute.Reorder -R 2-last,1 -i /home/shweta.pcs13/gen02/trainingB_$1_$2_s2w.arff -o /home/shweta.pcs13/gen02/trainingB_$1_$2_reorder.arff 
		java weka.filters.unsupervised.attribute.Reorder -R 2-last,1 -i /home/shweta.pcs13/gen02/testB_$1_$2_s2w.arff -o /home/shweta.pcs13/gen02/testB_$1_$2_reorder.arff
		# Build Model
		# ===========
		java weka.classifiers.functions.SMO -t /home/shweta.pcs13/gen02/trainingB_$1_$2_reorder.arff -d /home/shweta.pcs13/gen02/trainingB_$1_$2particle_smo_model.model
		# java weka.classifiers.bayes.NaiveBayes -t /home/shweta.pcs13/gen01/trainingB_$1_$2_reorder.arff -d /home/shweta.pcs13/gen01/trainingB_$1_$2particle_nb_model.model
		# Apply model on Test set
		# =======================
		java weka.classifiers.functions.SMO  -T /home/shweta.pcs13/gen02/testB_$1_$2_reorder.arff  -l   /home/shweta.pcs13/gen02/trainingB_$1_$2particle_smo_model.model -p 0 > /home/shweta.pcs13/gen02/result01/result_$1_$2_smo.txt
		java weka.classifiers.functions.SMO  -T /home/shweta.pcs13/gen02/testB_$1_$2_reorder.arff  -l   /home/shweta.pcs13/gen02/trainingB_$1_$2particle_smo_model.model -i > /home/shweta.pcs13/gen02/output01/output_$1_$2_smo.txt
		
		# java weka.classifiers.bayes.NaiveBayes  -T /home/shweta.pcs13/gen01/testB_$1_$2_reorder.arff  -l   /home/shweta.pcs13/gen01/trainingB_$1_$2particle_nb_model.model -p 0 > /home/shweta.pcs13/gen01/result01/result_$1_$2_nb.txt
		# java weka.classifiers.bayes.NaiveBayes  -T /home/shweta.pcs13/gen01/testB_$1_$2_reorder.arff  -l   /home/shweta.pcs13/gen01/trainingB_$1_$2particle_nb_model.model -i > /home/shweta.pcs13/gen01/output01/output_$1_$2_nb.txt
}
INERTIA_WEIGHT=0.25
NO_OF_ITERATION=10
NO_OF_PARTICLE=10
NO_OF_ATTRIBUTE=37
PHI1=2.1
PHI2=1.9
maxFGMValue=0.1
flag=0
midValue=0.5
export CLASSPATH=/usr/local/weka/weka-3-6-11/weka.jar
for (( particle=1; particle <= $NO_OF_PARTICLE; particle++ ))
do
for (( attribute=0; attribute <= $NO_OF_ATTRIBUTE+1; attribute++ )) 
do
	# generating random number
        random=$[(100 + (RANDOM % 100))]$[1000 + (RANDOM % 1000)]
        random=0.${random:1:2}${random:4:3}
	if [ $(echo "$random < $midValue"|bc) -eq 1 ]
        then
        	positionVector[$attribute]=0
		bestPositionVector[$attribute]=0
        else
                positionVector[$attribute]=1
		bestPositionVector[$attribute]=1
        fi
        velocityVector[$attribute]=$(echo "scale=3;$random*1"|bc)
done
	java -cp pssmo.jar TestAttribSelectSMO 0 $particle ${positionVector[@]}
	java -cp pssmo.jar TrainAttribSelectSMO 0 $particle ${positionVector[@]}
	getFMeasure 0 $particle
	newfMeasure=$(java -cp pssmo.jar FMeasureFinderSMO 0 $particle)
	if [ $(echo "$newfMeasure > $maxFGMValue"|bc) -eq 1 ]
	then
		maxFGMValue=$newfMeasure;
		particleNumber=$particle;
	fi
	# update bestPositionVector;
    echo 0 $particle ${bestPositionVector[2]} ${bestPositionVector[3]} ${bestPositionVector[4]} ${bestPositionVector[5]} ${bestPositionVector[6]} ${bestPositionVector[7]} ${bestPositionVector[8]} ${bestPositionVector[9]} ${bestPositionVector[10]} ${bestPositionVector[11]} ${bestPositionVector[12]} ${bestPositionVector[13]} ${bestPositionVector[14]} ${bestPositionVector[15]} ${bestPositionVector[16]} ${bestPositionVector[17]} ${bestPositionVector[18]} ${bestPositionVector[19]} ${bestPositionVector[20]} ${bestPositionVector[21]} ${bestPositionVector[22]} ${bestPositionVector[23]} ${bestPositionVector[24]} ${bestPositionVector[25]} ${bestPositionVector[26]} ${bestPositionVector[27]} ${bestPositionVector[28]} ${bestPositionVector[29]} ${bestPositionVector[30]} ${bestPositionVector[31]} ${bestPositionVector[32]} ${bestPositionVector[33]} ${bestPositionVector[34]} ${bestPositionVector[35]} ${bestPositionVector[36]} ${bestPositionVector[37]} ${bestPositionVector[38]} $newfMeasure >> bestPositionVectorPSO.txt
    # update positionVector;
	echo 0 $particle ${positionVector[2]} ${positionVector[3]} ${positionVector[4]} ${positionVector[5]} ${positionVector[6]} ${positionVector[7]} ${positionVector[8]} ${positionVector[9]} ${positionVector[10]} ${positionVector[11]} ${positionVector[12]} ${positionVector[13]} ${positionVector[14]} ${positionVector[15]} ${positionVector[16]} ${positionVector[17]} ${positionVector[18]} ${positionVector[19]} ${positionVector[20]} ${positionVector[21]} ${positionVector[22]} ${positionVector[23]} ${positionVector[24]} ${positionVector[25]} ${positionVector[26]} ${positionVector[27]} ${positionVector[28]} ${positionVector[29]} ${positionVector[30]} ${positionVector[31]} ${positionVector[32]} ${positionVector[33]} ${positionVector[34]} ${positionVector[35]} ${positionVector[36]} ${positionVector[37]} ${positionVector[38]} $newfMeasure >> positionVectorPSO.txt
	# update velocityVector;        
	echo 0 $particle ${velocityVector[2]} ${velocityVector[3]} ${velocityVector[4]} ${velocityVector[5]} ${velocityVector[6]} ${velocityVector[7]} ${velocityVector[8]} ${velocityVector[9]} ${velocityVector[10]} ${velocityVector[11]} ${velocityVector[12]} ${velocityVector[13]} ${velocityVector[14]} ${velocityVector[15]} ${velocityVector[16]} ${velocityVector[17]} ${velocityVector[18]} ${velocityVector[19]} ${velocityVector[20]} ${velocityVector[21]} ${velocityVector[22]} ${velocityVector[23]} ${velocityVector[24]} ${velocityVector[25]} ${velocityVector[26]} ${velocityVector[27]} ${velocityVector[28]} ${velocityVector[29]} ${velocityVector[30]} ${velocityVector[31]} ${velocityVector[32]} ${velocityVector[33]} ${velocityVector[34]} ${velocityVector[35]} ${velocityVector[36]} ${velocityVector[37]} ${velocityVector[38]}  >> velocityVectorPSO.txt
done
	# get velocity vector
	tempStr=$(sed $particleNumber!d positionVector.txt)
	globalBestPositionVector=(${tempStr// / })
	# update globalBestPositionVector;
echo 0 $particleNumber ${globalBestPositionVector[2]} ${globalBestPositionVector[3]} ${globalBestPositionVector[4]} ${globalBestPositionVector[5]} ${globalBestPositionVector[6]} ${globalBestPositionVector[7]} ${globalBestPositionVector[8]} ${globalBestPositionVector[9]} ${globalBestPositionVector[10]} ${globalBestPositionVector[11]} ${globalBestPositionVector[12]} ${globalBestPositionVector[13]} ${globalBestPositionVector[14]} ${globalBestPositionVector[15]} ${globalBestPositionVector[16]} ${globalBestPositionVector[17]} ${globalBestPositionVector[18]} ${globalBestPositionVector[19]} ${globalBestPositionVector[20]} ${globalBestPositionVector[21]} ${globalBestPositionVector[22]} ${globalBestPositionVector[23]} ${globalBestPositionVector[24]} ${globalBestPositionVector[25]} ${globalBestPositionVector[26]} ${globalBestPositionVector[27]} ${globalBestPositionVector[28]} ${globalBestPositionVector[29]} ${globalBestPositionVector[30]} ${globalBestPositionVector[31]} ${globalBestPositionVector[32]} ${globalBestPositionVector[33]} ${globalBestPositionVector[34]} ${globalBestPositionVector[35]} ${globalBestPositionVector[36]} ${globalBestPositionVector[37]} ${globalBestPositionVector[38]} $maxFGMValue >> globalBestPositionVector.txt
for (( iteration = 1; iteration <= $NO_OF_ITERATION; iteration++ ))
do
for (( particle = 1; particle <= $NO_OF_PARTICLE; particle++ ))
do
	lineNumber=$(echo "$NO_OF_PARTICLE*$iteration+$particle"|bc)
	# get velocity vector
	tempStr=$(sed $lineNumber!d velocityVectorPSO.txt)
	tempVelocityVector=(${tempStr// / })
	echo "tempVelocityVector:${tempVelocityVector[@]}"
	# get position vector
	tempStr=$(sed $lineNumber!d positionVectorPSO.txt)
	tempPositionVector=(${tempStr// / })
	echo "tempPositionVector:${tempPositionVector[@]}"
	# get best position vector
	tempStr=$(sed $lineNumber!d bestPositionVectorPSO.txt)
	bestPositionVector=(${tempStr// / })
	echo "bestPositionVector:${bestPositionVector[@]}"
	# get global best position vector
	tempStr=$(sed $iteration!d globalBestPositionVectorPSO.txt)
	globalBestPositionVector=(${tempStr// / })
	echo "globalBestPositionVector:${globalBestPositionVector[@]}"
for (( attrib = 0; attrib <= $NO_OF_ATTRIBUTE+1; attrib++ ))
do
	# calculating velocity vector using PSO formula
	echo "scale=3;$INERTIA_WEIGHT * ${tempVelocityVector[$attrib]} + $PHI1 * (${bestPositionVector[$attrib]} - ${tempPositionVector[$attrib]}) + $PHI2 * (${globalBestPositionVector[$attrib]} - ${tempPositionVector[$attrib]})"|bc
	velocityVector[$attrib]=$(echo "scale=3;$INERTIA_WEIGHT * ${tempVelocityVector[$attrib]} + $PHI1 * (${bestPositionVector[$attrib]} - ${tempPositionVector[$attrib]}) + $PHI2 * (${globalBestPositionVector[$attrib]} - ${tempPositionVector[$attrib]})"|bc)
					# using sigmoid function
					exp=$(echo "scale=3;e(-(${velocityVector[$attrib]}))"|bc -l)
					sigmoidValue=$(echo "scale=3;1/(1 + $exp)"|bc -l)
					# generating random number 
					random=$[(100 + (RANDOM % 100))]$[1000 + (RANDOM % 1000)]
					random=0.${random:1:2}${random:4:3}
					# calculating position vector using sigmoid value
					if [ $(echo "$random < $sigmoidValue"|bc) -eq 1 ]
					then
						positionVector[$attrib]=1;
					else
						positionVector[$attrib]=0;
					fi
done
				# call weka commands to execute clustering and get f-measure
				java -cp pssmo.jar TestAttribSelectSMO $iteration $particle ${positionVector[@]}
				java -cp pssmo.jar TrainAttribSelectSMO $iteration $particle ${positionVector[@]}
				getFMeasure $iteration $particle
				newfMeasure=$(java -cp pssmo.jar FMeasureFinderSMO $iteration $particle)
				if [ $(echo "$newfMeasure > $maxFGMValue"|bc) -eq 1 ]
				then
					maxFGMValue=$newfMeasure;
					flag=1;
					particleNumber=$particle;
				fi
				temp=$(echo "$NO_OF_ATTRIBUTE+2"|bc)
				if [  $(echo "$newfMeasure > ${tempPositionVector[$temp]}"|bc) -eq 1 ]
				then
					# update bestPositionVector as positionVector;
					echo $iteration $particle ${positionVector[2]} ${positionVector[3]} ${positionVector[4]} ${positionVector[5]} ${positionVector[6]} ${positionVector[7]} ${positionVector[8]} ${positionVector[9]} ${positionVector[10]} ${positionVector[11]} ${positionVector[12]} ${positionVector[13]} ${positionVector[14]} ${positionVector[15]} ${positionVector[16]} ${positionVector[17]} ${positionVector[18]} ${positionVector[19]} ${positionVector[20]} ${positionVector[21]} ${positionVector[22]} ${positionVector[23]} ${positionVector[24]} ${positionVector[25]} ${positionVector[26]} ${positionVector[27]} ${positionVector[28]} ${positionVector[29]} ${positionVector[30]} ${positionVector[31]} ${positionVector[32]} ${positionVector[33]} ${positionVector[34]} ${positionVector[35]} ${positionVector[36]} ${positionVector[37]} ${positionVector[38]} $newfMeasure >> bestPositionVectorPSO.txt
				else
					echo $iteration $particle ${bestPositionVector[2]} ${bestPositionVector[3]} ${bestPositionVector[4]} ${bestPositionVector[5]} ${bestPositionVector[6]} ${bestPositionVector[7]} ${bestPositionVector[8]} ${bestPositionVector[9]} ${bestPositionVector[10]} ${bestPositionVector[11]} ${bestPositionVector[12]} ${bestPositionVector[13]} ${bestPositionVector[14]} ${bestPositionVector[15]} ${bestPositionVector[16]} ${bestPositionVector[17]} ${bestPositionVector[18]} ${bestPositionVector[19]} ${bestPositionVector[20]} ${bestPositionVector[21]} ${bestPositionVector[22]} ${bestPositionVector[23]} ${bestPositionVector[24]} ${bestPositionVector[25]} ${bestPositionVector[26]} ${bestPositionVector[27]} ${bestPositionVector[28]} ${bestPositionVector[29]} ${bestPositionVector[30]} ${bestPositionVector[31]} ${bestPositionVector[32]} ${bestPositionVector[33]} ${bestPositionVector[34]} ${bestPositionVector[35]} ${bestPositionVector[36]} ${bestPositionVector[37]} ${bestPositionVector[38]} ${bestPositionVector[39]} >> bestPositionVectorPSO.txt
				fi
				# update positionVector;
				echo $iteration $particle ${positionVector[2]} ${positionVector[3]} ${positionVector[4]} ${positionVector[5]} ${positionVector[6]} ${positionVector[7]} ${positionVector[8]} ${positionVector[9]} ${positionVector[10]} ${positionVector[11]} ${positionVector[12]} ${positionVector[13]} ${positionVector[14]} ${positionVector[15]} ${positionVector[16]} ${positionVector[17]} ${positionVector[18]} ${positionVector[19]} ${positionVector[20]} ${positionVector[21]} ${positionVector[22]} ${positionVector[23]} ${positionVector[24]} ${positionVector[25]} ${positionVector[26]} ${positionVector[27]} ${positionVector[28]} ${positionVector[29]} ${positionVector[30]} ${positionVector[31]} ${positionVector[32]} ${positionVector[33]} ${positionVector[34]} ${positionVector[35]} ${positionVector[36]} ${positionVector[37]} ${positionVector[38]} $newfMeasure >> positionVector.txt
				# update velocityVector;
				echo $iteration $particle ${velocityVector[2]} ${velocityVector[3]} ${velocityVector[4]} ${velocityVector[5]} ${velocityVector[6]} ${velocityVector[7]} ${velocityVector[8]} ${velocityVector[9]} ${velocityVector[10]} ${velocityVector[11]} ${velocityVector[12]} ${velocityVector[13]} ${velocityVector[14]} ${velocityVector[15]} ${velocityVector[16]} ${velocityVector[17]} ${velocityVector[18]} ${velocityVector[19]} ${velocityVector[20]} ${velocityVector[21]} ${velocityVector[22]} ${velocityVector[23]} ${velocityVector[24]} ${velocityVector[25]} ${velocityVector[26]} ${velocityVector[27]} ${velocityVector[28]} ${velocityVector[29]} ${velocityVector[30]} ${velocityVector[31]} ${velocityVector[32]} ${velocityVector[33]} ${velocityVector[34]} ${velocityVector[35]} ${velocityVector[36]} ${velocityVector[37]} ${velocityVector[38]} >> velocityVector.txt
				# display updated information;
				echo "Particle-$particle and Iteration-$iteration Position Vector"
				echo ${positionVector[@]}
				echo "Particle-$particle and Iteration-$iteration Velocity Vector"
				echo ${velocityVector[@]}
				echo "F-Measure for Particle-$particle and Iteration-$iteration =$newfMeasure"
done
			# update globalBestPositionVector;
			if [ $flag -eq 1 ]
			then
			lineNumber=$(echo "$NO_OF_PARTICLE*$iteration+$particleNumber"|bc)
			# get position vector
			tempStr=$(sed $lineNumber!d positionVectorPSO.txt)
			globalBestPositionVector=(${tempStr// / })
			echo $iteration $particleNumber ${globalBestPositionVector[2]} ${globalBestPositionVector[3]} ${globalBestPositionVector[4]} ${globalBestPositionVector[5]} ${globalBestPositionVector[6]} ${globalBestPositionVector[7]} ${globalBestPositionVector[8]} ${globalBestPositionVector[9]} ${globalBestPositionVector[10]} ${globalBestPositionVector[11]} ${globalBestPositionVector[12]} ${globalBestPositionVector[13]} ${globalBestPositionVector[14]} ${globalBestPositionVector[15]} ${globalBestPositionVector[16]} ${globalBestPositionVector[17]} ${globalBestPositionVector[18]} ${globalBestPositionVector[19]} ${globalBestPositionVector[20]} ${globalBestPositionVector[21]} ${globalBestPositionVector[22]} ${globalBestPositionVector[23]} ${globalBestPositionVector[24]} ${globalBestPositionVector[25]} ${globalBestPositionVector[26]} ${globalBestPositionVector[27]} ${globalBestPositionVector[28]} ${globalBestPositionVector[29]} ${globalBestPositionVector[30]} ${globalBestPositionVector[31]} ${globalBestPositionVector[32]} ${globalBestPositionVector[33]} ${globalBestPositionVector[34]} ${globalBestPositionVector[35]} ${globalBestPositionVector[36]} ${globalBestPositionVector[37]} ${globalBestPositionVector[38]} $maxFGMValue >> globalBestPositionVectorPSO.txt
			flag=0
			else
			echo $iteration $particleNumber ${globalBestPositionVector[2]} ${globalBestPositionVector[3]} ${globalBestPositionVector[4]} ${globalBestPositionVector[5]} ${globalBestPositionVector[6]} ${globalBestPositionVector[7]} ${globalBestPositionVector[8]} ${globalBestPositionVector[9]} ${globalBestPositionVector[10]} ${globalBestPositionVector[11]} ${globalBestPositionVector[12]} ${globalBestPositionVector[13]} ${globalBestPositionVector[14]} ${globalBestPositionVector[15]} ${globalBestPositionVector[16]} ${globalBestPositionVector[17]} ${globalBestPositionVector[18]} ${globalBestPositionVector[19]} ${globalBestPositionVector[20]} ${globalBestPositionVector[21]} ${globalBestPositionVector[22]} ${globalBestPositionVector[23]} ${globalBestPositionVector[24]} ${globalBestPositionVector[25]} ${globalBestPositionVector[26]} ${globalBestPositionVector[27]} ${globalBestPositionVector[28]} ${globalBestPositionVector[29]} ${globalBestPositionVector[30]} ${globalBestPositionVector[31]} ${globalBestPositionVector[32]} ${globalBestPositionVector[33]} ${globalBestPositionVector[34]} ${globalBestPositionVector[35]} ${globalBestPositionVector[36]} ${globalBestPositionVector[37]} ${globalBestPositionVector[38]} ${globalBestPositionVector[39]} >> globalBestPositionVectorPSO.txt
			fi
done
