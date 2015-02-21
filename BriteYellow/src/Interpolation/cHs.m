function [f1, f2] = cHs(t, Speed0, Heading0, Speed1, Heading1)


H0i = Speed0*cos(Heading0);
H0j = Speed0*sin(Heading0);

H1i = Speed1*cos(Heading1);
H1j = Speed1*sin(Heading1);

M0i = 0;
M0j = 2;

M1i = 1;
M1j = 1;

f1 = (2*t^3 - 3*t^2 + 1) * M0i + (t^3 - 2*t^2 + t)*H0i + (-2*t^3 + 3*t^2)*M1i + (t^3-t^2)*H1i;
f2 = (2*t^3 - 3*t^2 + 1) * M0j + (t^3 - 2*t^2 + t)*H0j + (-2*t^3 + 3*t^2)*M1j + (t^3-t^2)*H1j;
