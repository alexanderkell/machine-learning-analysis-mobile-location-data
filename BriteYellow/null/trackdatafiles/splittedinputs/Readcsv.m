fid = fopen('200,302,330,322.csv');  %open file
data = textscan(fid, '%s %d %d %d %d %d %d %d %d %d %d %d %d %d %d %d %d %d', 'Delimiter',',', ...
    'MultipleDelimsAsOne',true, 'CollectOutput',false);
fclose(fid);
entries = regexp(data, ',', 'split'); %This will return a cell array with the individual entries for each string you have between the commas.