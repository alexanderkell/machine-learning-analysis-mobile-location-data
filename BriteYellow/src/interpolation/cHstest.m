

Speed0 = 1;
Heading0 = 0;

Speed1 = 2;
Heading1 = 0;

j = 1;
for i = 0:0.05:1
[f1(j),f2(j)] = cHs(i,Speed0, Heading0, Speed1, Heading1)
j = j+1;
end

plot(f1,f2, '--b');
