Section 1: USING THE PROGRAM

   1. Train

   Option A (with GUI): 
      1. Run "java svm_helper" 
      2. Make sure the "Train" tab is selected
      3. Fill in the blanks (the optional fields can be left blank)
      4. Click the big "Train" button and wait for training finishes
      5. A ".model" file will be created in the same directory as your training file
      6. Optional - for plotting hyper planes:
	 a. In the results tab which is just created, click "Show graph"
         b. A new window containing the plot will pop up.

 
   Option B (without GUI):
      1. Run "java svm_train [OPTIONS] [TRAIN_FILE]"
      2. A ".model" file will be created in the same directory as your training file
      3. Optional - for plotting hyper planes:
	 a. Run "java plot_predictions [TRAIN_FILE] [MODEL_FILE]"
         b. A new window containing the plot will pop up.

         Parameters: 
          + OPTIONS (optional): e.g. "-t 2 -c 100" see http://www.csie.ntu.edu.tw/~cjlin/libsvm/
          + TRAIN_FILE: your training file e.g. "xxxx.train", when required enter its full/absolute path
          + MODEL_FILE: the file "xxxx.model" created during Step 5 in Option A or Step 2 in Option B, 
                        when required enter its full/absolute path

============================================================================================================
   2. Predict

   Option A (With GUI): 
      1. Run "java svm_helper" 
      2. Select the "Predict" tab
      3. Fill in the blanks (the optional fields can be left blank)
         a. Optional: The output filepath field can be left blank or filled,
            if blank, an "xxxx.out" file will be created in the same directory as the MODEL_FILE
	    otherwise, a new file will be created in the location specified
      4. Click the big "Predict" button and wait for training finishes
      
 
   Option B (Without GUI):
      1. Run "java svm_predict [OPTIONS] [TEST_FILE] [MODEL_FILE] [OUTPUT_FILE]"
      2. An output file will be created

         Parameters: 
           + OPTIONS (optional):
                "-b": probability_estimates - 1 for activating predict probability estimates, 0 for not 
                     activating (default 0); one-class SVM not supported yet
                "-q": quiet mode (no outputs)
           + TEST_FILE: your test file e.g. "xxxx.train", when required enter its full/absolute path
           + MODEL_FILE: the file "xxxx.model" created during Step 5 in Option A or Step 2 in Option B in 
                the training section, when required enter its full/absolute path
	   + OUTPUT_FILE: the location (full/absolute path) where you want the result file to be store, 
                optional for Option A but mandatory for Option B

==========================================================================================================
Section 2: TRAIN, TEST, OUTPUT FILE FORMAT

    + Format of training and testing data file: <label> <index1>:<value1> <index2>:<value2>
          + All labels and values must be in the float format, all indices must be in the integer format
    + Format of output data file: <predicted label>
    

    See the files in the "Sample" folder for more examples

==========================================================================================================
For more info, visit: http://www.csie.ntu.edu.tw/~cjlin/libsvm/

Readme file last modified: 2014-12-23 4:39 PM