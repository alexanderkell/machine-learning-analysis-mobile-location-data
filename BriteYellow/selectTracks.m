function result = selectTracks(A)

result = [];

count = 1;

track_no = 10;
if track_no >= A(1,6) && track_no <= Security0(length(A),6)
    for i = 1:length(A)
        if Security0(i,6) == track_no
            result(count, :) = A(i, :);
            count = count + 1;
        end

    end
else
    disp('Track number must be between ')
    disp(Security0(1,6))
    disp('and ')
    disp(Security0(length(Security0),6))
end
