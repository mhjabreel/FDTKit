# FDTKit
FDTKit is a fuzzy decision tree toolkit includes the state of art fuzzy decision tree algorithms

### Author: ###
Mohammed H. Jabreel <br/>
E-mail: mhjabreel@gmail.com <br/>
Researchgate: https://www.researchgate.net/profile/Mohammed_Jabreel
###  Algorithms: ###

### Induction of fuzzy decision trees (1995) ###
Yufei Yuan, Michael J. Shaw <br/>
http://www.sciencedirect.com/science/article/pii/016501149400229Z

### Fuzzy ID3 Algorithm Based on Generating Hartley Measure (2011) ###
Fachao Li, Dandan Jiang<br/>
http://link.springer.com/chapter/10.1007%2F978-3-642-23982-3_24

### A generalized fuzzy ID3 algorithm using generalized information entropy (2014) ### 
Chenxia Jina, Fachao Lia, Yan Lib<br/>
http://link.springer.com/chapter/10.1007%2F978-3-642-23982-3_24

### Example Output: ###
The ouput of the example file "com\fdtkit\fuzzy\fuzzydt\Example.java" is:
```
Ambiguity induction fuzzy decision tree
|Outlook|
	<Sunny>
		|Temperature|
			<Hot>
					[Swimming](0.86)
			<Mild>
				|Humidity|
					<Humid>
							[Volleyball](0.85)
					<Normal>
							[Swimming](0.82)
			<Cool>
					[Volleyball](0.93)
	<Cloudy>
		|Temperature|
			<Hot>
				|Humidity|
					<Humid>
							[Swimming](0.89)
					<Normal>
						|Wind|
							<Windy>
									[Volleyball](0.82)
							<Not-windy>
									[Swimming](0.64)
			<Mild>
				|Humidity|
					<Humid>
							[Volleyball](0.87)
					<Normal>
						|Wind|
							<Windy>
									[Volleyball](0.83)
							<Not-windy>
									[Volleyball](0.61)
			<Cool>
					[Volleyball](0.90)
	<Rain>
			[Weightlifting](0.91)
IF Outlook IS Sunny AND Temperature IS Hot THEN Swimming (0.86)
IF Outlook IS Sunny AND Temperature IS Mild AND Humidity IS Humid THEN Volleyball (0.85)
IF Outlook IS Sunny AND Temperature IS Mild AND Humidity IS Normal THEN Swimming (0.82)
IF Outlook IS Sunny AND Temperature IS Cool THEN Volleyball (0.93)
IF Outlook IS Cloudy AND Temperature IS Hot AND Humidity IS Humid THEN Swimming (0.89)
IF Outlook IS Cloudy AND Temperature IS Hot AND Humidity IS Normal AND Wind IS Windy THEN Volleyball (0.82)
IF Outlook IS Cloudy AND Temperature IS Hot AND Humidity IS Normal AND Wind IS Not-windy THEN Swimming (0.64)
IF Outlook IS Cloudy AND Temperature IS Mild AND Humidity IS Humid THEN Volleyball (0.87)
IF Outlook IS Cloudy AND Temperature IS Mild AND Humidity IS Normal AND Wind IS Windy THEN Volleyball (0.83)
IF Outlook IS Cloudy AND Temperature IS Mild AND Humidity IS Normal AND Wind IS Not-windy THEN Volleyball (0.61)
IF Outlook IS Cloudy AND Temperature IS Cool THEN Volleyball (0.90)
IF Outlook IS Rain THEN Weightlifting (0.91)

Trainingset Prediction:
0.20     0.70     0.00     
0.40     0.60     0.00     
0.30     0.70     0.00     
0.70     0.20     0.00     
0.70     0.10     0.00     
0.30     0.00     0.70     
0.00     0.00     1.00     
0.90     0.00     0.10     
0.00     1.00     0.00     
0.20     0.30     0.70     
0.30     0.60     0.00     
0.60     0.20     0.00     
0.30     0.70     0.10     
0.60     0.10     0.10     
0.30     0.00     0.70     
0.00     0.50     0.00     
FID3 fuzzy decision tree
|Outlook|
	<Sunny>
		|Temperature|
			<Hot>
					[Swimming](0.86)
			<Mild>
				|Humidity|
					<Humid>
							[Volleyball](0.85)
					<Normal>
							[Swimming](0.82)
			<Cool>
					[Volleyball](0.93)
	<Cloudy>
		|Wind|
			<Windy>
					[Volleyball](0.90)
			<Not-windy>
				|Humidity|
					<Humid>
							[Volleyball](0.89)
					<Normal>
						|Temperature|
							<Hot>
									[Swimming](0.64)
							<Mild>
									[Volleyball](0.61)
							<Cool>
									[Volleyball](0.89)
	<Rain>
			[Weightlifting](0.91)
IF Outlook IS Sunny AND Temperature IS Hot THEN Swimming (0.86)
IF Outlook IS Sunny AND Temperature IS Mild AND Humidity IS Humid THEN Volleyball (0.85)
IF Outlook IS Sunny AND Temperature IS Mild AND Humidity IS Normal THEN Swimming (0.82)
IF Outlook IS Sunny AND Temperature IS Cool THEN Volleyball (0.93)
IF Outlook IS Cloudy AND Wind IS Windy THEN Volleyball (0.90)
IF Outlook IS Cloudy AND Wind IS Not-windy AND Humidity IS Humid THEN Volleyball (0.89)
IF Outlook IS Cloudy AND Wind IS Not-windy AND Humidity IS Normal AND Temperature IS Hot THEN Swimming (0.64)
IF Outlook IS Cloudy AND Wind IS Not-windy AND Humidity IS Normal AND Temperature IS Mild THEN Volleyball (0.61)
IF Outlook IS Cloudy AND Wind IS Not-windy AND Humidity IS Normal AND Temperature IS Cool THEN Volleyball (0.89)
IF Outlook IS Rain THEN Weightlifting (0.91)

Trainingset Prediction:
0.20     0.70     0.00     
0.40     0.60     0.00     
0.30     0.70     0.00     
0.70     0.20     0.00     
0.70     0.10     0.00     
0.30     0.00     0.70     
0.00     0.00     1.00     
0.90     0.00     0.10     
0.00     1.00     0.00     
0.30     0.10     0.70     
0.30     0.60     0.00     
0.70     0.20     0.00     
0.30     0.70     0.10     
0.70     0.10     0.10     
0.30     0.00     0.70     
0.50     0.50     0.00     
GFID3 fuzzy decision tree, with linear maping function {I(t) = t}
|Outlook|
	<Sunny>
		|Humidity|
			<Humid>
				|Temperature|
					<Hot>
							[Swimming](0.93)
					<Mild>
							[Volleyball](0.85)
					<Cool>
							[Volleyball](0.92)
			<Normal>
				|Temperature|
					<Hot>
							[Swimming](0.94)
					<Mild>
							[Swimming](0.82)
					<Cool>
							[Volleyball](0.92)
	<Cloudy>
		|Temperature|
			<Hot>
				|Humidity|
					<Humid>
							[Swimming](0.89)
					<Normal>
						|Wind|
							<Windy>
									[Volleyball](0.82)
							<Not-windy>
									[Swimming](0.64)
			<Mild>
				|Humidity|
					<Humid>
							[Volleyball](0.87)
					<Normal>
						|Wind|
							<Windy>
									[Volleyball](0.83)
							<Not-windy>
									[Volleyball](0.61)
			<Cool>
					[Volleyball](0.90)
	<Rain>
			[Weightlifting](0.91)
IF Outlook IS Sunny AND Humidity IS Humid AND Temperature IS Hot THEN Swimming (0.93)
IF Outlook IS Sunny AND Humidity IS Humid AND Temperature IS Mild THEN Volleyball (0.85)
IF Outlook IS Sunny AND Humidity IS Humid AND Temperature IS Cool THEN Volleyball (0.92)
IF Outlook IS Sunny AND Humidity IS Normal AND Temperature IS Hot THEN Swimming (0.94)
IF Outlook IS Sunny AND Humidity IS Normal AND Temperature IS Mild THEN Swimming (0.82)
IF Outlook IS Sunny AND Humidity IS Normal AND Temperature IS Cool THEN Volleyball (0.92)
IF Outlook IS Cloudy AND Temperature IS Hot AND Humidity IS Humid THEN Swimming (0.89)
IF Outlook IS Cloudy AND Temperature IS Hot AND Humidity IS Normal AND Wind IS Windy THEN Volleyball (0.82)
IF Outlook IS Cloudy AND Temperature IS Hot AND Humidity IS Normal AND Wind IS Not-windy THEN Swimming (0.64)
IF Outlook IS Cloudy AND Temperature IS Mild AND Humidity IS Humid THEN Volleyball (0.87)
IF Outlook IS Cloudy AND Temperature IS Mild AND Humidity IS Normal AND Wind IS Windy THEN Volleyball (0.83)
IF Outlook IS Cloudy AND Temperature IS Mild AND Humidity IS Normal AND Wind IS Not-windy THEN Volleyball (0.61)
IF Outlook IS Cloudy AND Temperature IS Cool THEN Volleyball (0.90)
IF Outlook IS Rain THEN Weightlifting (0.91)

Trainingset Prediction:
0.20     0.70     0.00     
0.40     0.60     0.00     
0.30     0.70     0.00     
0.70     0.20     0.00     
0.50     0.10     0.00     
0.30     0.00     0.70     
0.00     0.00     1.00     
0.90     0.00     0.10     
0.00     0.60     0.00     
0.20     0.30     0.70     
0.30     0.60     0.00     
0.60     0.20     0.00     
0.30     0.70     0.10     
0.60     0.10     0.10     
0.30     0.00     0.70     
0.00     0.50     0.00     
GFID3 fuzzy decision tree, with quadratic maping function {I(t) = t^2}
|Outlook|
	<Sunny>
		|Humidity|
			<Humid>
				|Temperature|
					<Hot>
							[Swimming](0.90)
					<Mild>
							[Volleyball](0.87)
					<Cool>
							[Volleyball](0.97)
			<Normal>
				|Temperature|
					<Hot>
							[Swimming](0.96)
					<Mild>
						|Wind|
							<Windy>
									[Volleyball](0.81)
							<Not-windy>
									[Swimming](0.75)
					<Cool>
							[Volleyball](0.98)
	<Cloudy>
		|Temperature|
			<Hot>
				|Wind|
					<Windy>
							[Volleyball](0.85)
					<Not-windy>
						|Humidity|
							<Humid>
									[Volleyball](0.83)
							<Normal>
									[Weightlifting](0.63)
			<Mild>
				|Humidity|
					<Humid>
							[Volleyball](0.93)
					<Normal>
						|Wind|
							<Windy>
									[Volleyball](0.80)
							<Not-windy>
									[Weightlifting](0.55)
			<Cool>
					[Volleyball](0.88)
	<Rain>
			[Weightlifting](0.89)
IF Outlook IS Sunny AND Humidity IS Humid AND Temperature IS Hot THEN Swimming (0.90)
IF Outlook IS Sunny AND Humidity IS Humid AND Temperature IS Mild THEN Volleyball (0.87)
IF Outlook IS Sunny AND Humidity IS Humid AND Temperature IS Cool THEN Volleyball (0.97)
IF Outlook IS Sunny AND Humidity IS Normal AND Temperature IS Hot THEN Swimming (0.96)
IF Outlook IS Sunny AND Humidity IS Normal AND Temperature IS Mild AND Wind IS Windy THEN Volleyball (0.81)
IF Outlook IS Sunny AND Humidity IS Normal AND Temperature IS Mild AND Wind IS Not-windy THEN Swimming (0.75)
IF Outlook IS Sunny AND Humidity IS Normal AND Temperature IS Cool THEN Volleyball (0.98)
IF Outlook IS Cloudy AND Temperature IS Hot AND Wind IS Windy THEN Volleyball (0.85)
IF Outlook IS Cloudy AND Temperature IS Hot AND Wind IS Not-windy AND Humidity IS Humid THEN Volleyball (0.83)
IF Outlook IS Cloudy AND Temperature IS Hot AND Wind IS Not-windy AND Humidity IS Normal THEN Weightlifting (0.63)
IF Outlook IS Cloudy AND Temperature IS Mild AND Humidity IS Humid THEN Volleyball (0.93)
IF Outlook IS Cloudy AND Temperature IS Mild AND Humidity IS Normal AND Wind IS Windy THEN Volleyball (0.80)
IF Outlook IS Cloudy AND Temperature IS Mild AND Humidity IS Normal AND Wind IS Not-windy THEN Weightlifting (0.55)
IF Outlook IS Cloudy AND Temperature IS Cool THEN Volleyball (0.88)
IF Outlook IS Rain THEN Weightlifting (0.89)

Trainingset Prediction:
0.20     0.70     0.00     
0.40     0.60     0.10     
0.30     0.70     0.20     
0.70     0.20     0.20     
0.50     0.10     0.10     
0.30     0.00     0.70     
0.00     0.00     1.00     
0.10     0.00     0.90     
0.00     0.60     0.00     
0.30     0.00     0.70     
0.30     0.60     0.00     
0.60     0.00     0.30     
0.10     0.00     0.70     
0.60     0.00     0.30     
0.30     0.00     0.70     
0.50     0.50     0.00     
```
