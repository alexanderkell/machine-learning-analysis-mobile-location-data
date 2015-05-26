%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% Track Generation
%% Name: Fezan Bux
%% Date: 13%04%2015
%% Date of Update: 23%04%2015
%% Description: K-means 3D plot
%% Reference: Chapter 7.3 Report or User Guide
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%load the data sets
X = [idx D];
s=15;   %size for the points

%assign data points with relative phone ID
X_1 = X(X(:,1)==1,:);   % = 1 security
X_2 = X(X(:,1)==2,:);   % = 2 shopper
X_3 = X(X(:,1)==3,:);   % = 3 business

%plot the data points in turns
scatter3(X_1(:,2),X_1(:,3),X_1(:,4),s,'b','filled') %security blue
hold on
scatter3(X_2(:,2),X_2(:,3),X_2(:,4),s,'r','filled') %shopper red
scatter3(X_3(:,2),X_3(:,3),X_3(:,4),s,'k','filled') %business black
hold off
title('data reduction kmeans')
grid on


