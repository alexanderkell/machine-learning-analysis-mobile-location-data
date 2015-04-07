% Code below modified from:
% http://stackoverflow.com/questions/2649384/svm-visualization-in-matlab
% Files: libsvmread.mexw32, libsvmwrite.mexw32, svmpredict.mexw32,
% svmtrain.mexw32
% available at http://www.nyuinformatics.org/downloads/tmp/libsvm/

clear all

[trainlabels, trainfeatures] = libsvmread('Samples/atesting1.train');

model = svmtrain(trainlabels, trainfeatures, '-t 1 -c 100');


% Labels are -1 or 1

figure

% plot training data
hold on;

pos = find(trainlabels==-1);
scatter(trainfeatures(pos,1), trainfeatures(pos,2), 'b')
pos = find(trainlabels==1);
scatter(trainfeatures(pos,1), trainfeatures(pos,2), 'r')

% now plot support vectors
hold on;
sv = full(model.SVs);
plot(sv(:,1),sv(:,2),'ko');

%% now plot decision area

% 1. calculate the decision area
[xi,yi] = meshgrid(min(trainfeatures(:,1)):(max(trainfeatures(:,1)-min(trainfeatures(:,1))))/200:max(trainfeatures(:,1)) , min(trainfeatures(:,2)):(max(trainfeatures(:,2)- min(trainfeatures(:,2))))/200:max(trainfeatures(:,2)));
dd = [xi(:),yi(:)];
% Start timing
tic;
[predicted_label, accuracy, decision_values] = svmpredict(ones(size(dd,1),1), dd, model); % The percentage accuracy will be the percent of +1 points
% Stop timing
toc

% 2. plot the area
disp('Ploting the meshgrid. May take a while...')
pos = find(predicted_label==1);
hold on;

% Define the colours
redcolor = [1 0.7 0.7]; % red color
bluecolor = [0.7 0.7 1];  % blue color

% Plot the background (which is made of lots of predicted points)
h1 = plot(dd(pos,1),dd(pos,2),'s','color',redcolor,'MarkerSize',10,'MarkerEdgeColor',redcolor,'MarkerFaceColor',redcolor);
pos = find(predicted_label==-1);
hold on;
h2 = plot(dd(pos,1),dd(pos,2),'s','color',bluecolor,'MarkerSize',10,'MarkerEdgeColor',bluecolor,'MarkerFaceColor',bluecolor);

% Send the meshgrid to bottom so the training data can be seen
uistack(h1, 'bottom');
uistack(h2, 'bottom');

xlabel('x1');
ylabel('x2');
legend('-1 Area','+1 Area', '-1 train data', '+1 train data','SV','Location','southoutside','Orientation','horizontal')