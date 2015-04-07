% Code below modified from:
% http://stackoverflow.com/questions/2649384/svm-visualization-in-matlab
% Files: libsvmread.mexw32, libsvmwrite.mexw32, svmpredict.mexw32,
% svmtrain.mexw32
% available at http://www.nyuinformatics.org/downloads/tmp/libsvm/

clear all

[trainlabels, trainfeatures] = libsvmread('atesting1.train');
%{
colA1 = trainfeatures(:,1);
colA2 = trainfeatures(:,2);

count1=0;
count2=0;

for i=1: size(colA1)
    
    if trainlabels(i)==1
        count1 = count1 + 1;
        colB1(count1) = colA1(i);
        colB2(count1) = colA2(i);
    else
        count2 = count2 + 1;
        colC1(count2) = colA1(i);
        colC2(count2) = colA2(i);
    end
end

plot(colB1,colB2,'r','LineStyle', 'none', 'marker', '.', 'MarkerSize', 8)
grid on
hold on
plot(colC1,colC2,'b','LineStyle', 'none', 'marker', '.', 'MarkerSize', 8)
hold off

%}
model = svmtrain(trainlabels, trainfeatures, '-t 1 -c 100');

%{
w = model.SVs' * model.sv_coef;
b = -model.rho;
if (model.Label(1) == -1)
    w = -w; b = -b;
end
%}

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
redcolor = [1 0.8 0.8];
bluecolor = [0.8 0.8 1];
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