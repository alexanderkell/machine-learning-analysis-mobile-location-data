
[FileName,PathName] = uigetfile('*.csv','Select a file');

FILENAME = strcat(PathName, FileName);
    
fid = fopen(FILENAME);

% Chart title, xaxis, yaxis
tit = fgetl(fid);
xaxis = fgetl(fid);
yaxis = fgetl(fid);

% get the labels and legends
labels = strsplit(fgetl(fid), ',');

legends = strsplit(fgetl(fid), ',');

% create an array to store the values
pro = zeros(length(legends),length(labels));

% get the values
for i = 1 : length(legends)
    temp = strsplit(fgetl(fid), ',');

    for j = 1:length(labels)
        pro(i,j) = str2double(temp(j));
    end
    
end

   figure
        bar(transpose(pro))
        xlabel(xaxis)
        ylabel(yaxis)
        title(tit)
        legend(legends)
        set(gca, 'XTick', 1:length(labels), 'XTickLabel', labels);