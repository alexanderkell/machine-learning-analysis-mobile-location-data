%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% Track Generation
%% Name: Fezan Bux
%% Date: 13%04%2015
%% Date of Update: 23%04%2015
%% Description: Kmeans 2D plot
%% Reference: Chapter 7.3 Report or User Guide
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
X=[pca1 pca2] %load the data sets
opts = statset('Display','final');
[idx,C] = kmeans(X,3,'start',loc2,'display','iter','maxiter',1)

%plot the data points in turns
figure;
plot(X(idx==1,1),X(idx==1,2),'r.','MarkerSize',12)
hold on
plot(X(idx==2,1),X(idx==2,2),'b.','MarkerSize',12)
plot(X(idx==3,1),X(idx==3,2),'g.','MarkerSize',12)
plot(C(:,1),C(:,2),'kx',...
     'MarkerSize',15,'LineWidth',3)
title 'Cluster Assignments and Centroids'
grid on
hold off