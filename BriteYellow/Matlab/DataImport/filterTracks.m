% This code imports CSV and filters out unwanted tracks

From200330to302322 = importCSV('/Users/Alexander/4th-year-project/BriteYellow/Matlab/Statistics/From (200, 330) to (302, 322).csv', 2, 211);
From200330to322344 = importCSV('/Users/Alexander/4th-year-project/BriteYellow/Matlab/Statistics/From (200, 330) to (322, 344).csv', 2, 211);
From200330to344364 = importCSV('/Users/Alexander/4th-year-project/BriteYellow/Matlab/Statistics/From (200, 330) to (344, 364).csv', 2, 211);
From200850to302364 = importCSV('/Users/Alexander/4th-year-project/BriteYellow/Matlab/Statistics/From (200, 850) to (302, 364).csv', 2, 211);
From330460to302322 = importCSV('/Users/Alexander/4th-year-project/BriteYellow/Matlab/Statistics/From (330, 460) to (302, 322).csv', 2, 211);
From330460to322344 = importCSV('/Users/Alexander/4th-year-project/BriteYellow/Matlab/Statistics/From (330, 460) to (322, 344).csv', 2, 211);
From330460to344364 = importCSV('/Users/Alexander/4th-year-project/BriteYellow/Matlab/Statistics/From (330, 460) to (344, 364).csv', 2, 211);
From460590to302322 = importCSV('/Users/Alexander/4th-year-project/BriteYellow/Matlab/Statistics/From (460, 590) to (302, 322).csv', 2, 211);
From460590to322344 = importCSV('/Users/Alexander/4th-year-project/BriteYellow/Matlab/Statistics/From (460, 590) to (322, 344).csv', 2, 211);
From460590to344364 = importCSV('/Users/Alexander/4th-year-project/BriteYellow/Matlab/Statistics/From (460, 590) to (344, 364).csv', 2, 211);
From590720to302322 = importCSV('/Users/Alexander/4th-year-project/BriteYellow/Matlab/Statistics/From (590, 720) to (302, 322).csv', 2, 211);
From590720to322344 = importCSV('/Users/Alexander/4th-year-project/BriteYellow/Matlab/Statistics/From (590, 720) to (322, 344).csv', 2, 211);
From590720to344364 = importCSV('/Users/Alexander/4th-year-project/BriteYellow/Matlab/Statistics/From (590, 720) to (344, 364).csv', 2, 211);
From720850to302322 = importCSV('/Users/Alexander/4th-year-project/BriteYellow/Matlab/Statistics/From (720, 850) to (302, 322).csv', 2, 211);
From720850to322344 = importCSV('/Users/Alexander/4th-year-project/BriteYellow/Matlab/Statistics/From (720, 850) to (322, 344).csv', 2, 211);
From720850to344364 = importCSV('/Users/Alexander/4th-year-project/BriteYellow/Matlab/Statistics/From (720, 850) to (344, 364).csv', 2, 211);

From200330to302322(strcmp(From200330to302322(:,1),1) &([From200330to302322{:,2}]'=4),:) = []
