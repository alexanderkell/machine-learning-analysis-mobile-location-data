% fit into the equation

function y = getY(poly, x)

length = size(poly);

y = 0;


for i = 1:length
    p = poly(i);
%x

    abc = p * x^a;

    y = y + p*abc;

 

end

