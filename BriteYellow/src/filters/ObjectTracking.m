%Student Dave's tutorial on:  Object tracking in image using 2-D kalman filter
%HEXBUG TRACKING!
%Copyright Student Dave's Tutorials 2012
%if you would like to use this code, please feel free, just remember to
%reference and tell your friends! :)

%here we take the hexbug extracted coordinates and apply the kalman fiter
%to the 2-dimensions of motion  within the image
%to see if it can do better than the tracking alone
%requires matlabs image processing toolbox.

clear all;
close all;
clc;
set(0,'DefaultFigureWindowStyle','docked') %dock the figures..just a personal preference you don't need this.
base_dir = 'E:\Dropbox\Student_dave\hexbug_frames\';
cd(base_dir);

%% get listing of frames
f_list =  dir('*png');

%% load tracking data
%load('CM_idx_easier.mat') %simple tracking: continuously monitored bug
%load('CM_idx_harder.mat') %hard tracking:  segment of very noisy images and very bad tracking
load('CM_idx_no.mat'); %missing data tracking: segment with no tracking



%% define main variables
dt = 1;  %our sampling rate
S_frame = 10; %starting frame
u = .005; % define acceleration magnitude
Q= [CM_idx(S_frame,1); CM_idx(S_frame,2); 0; 0]; %initized state--it has four components: [positionX; positionY; velocityX; velocityY] of the hexbug
Q_estimate = Q;  %estimate of initial location estimation of where the hexbug is (what we are updating)
HexAccel_noise_mag = .1; %process noise: the variability in how fast the Hexbug is speeding up (stdv of acceleration: meters/sec^2)
tkn_x = 1;  %measurement noise in the horizontal direction (x axis).
tkn_y = 1;  %measurement noise in the horizontal direction (y axis).
Ez = [tkn_x 0; 0 tkn_y];
Ex = [dt^4/4 0 dt^3/2 0; ...
    0 dt^4/4 0 dt^3/2; ...
    dt^3/2 0 dt^2 0; ...
    0 dt^3/2 0 dt^2].*HexAccel_noise_mag^2; % Ex convert the process noise (stdv) into covariance matrix
P = Ex; % estimate of initial Hexbug position variance (covariance matrix)

%% Define update equations in 2-D! (Coefficent matrices): A physics based model for where we expect the HEXBUG to be [state transition (state + velocity)] + [input control (acceleration)]
A = [1 0 dt 0; 0 1 0 dt; 0 0 1 0; 0 0 0 1]; %state update matrice
B = [(dt^2/2); (dt^2/2); dt; dt];
C = [1 0 0 0; 0 1 0 0];  %this is our measurement function C, that we apply to the state estimate Q to get our expect next/new measurement


%% initize result variables
% Initialize for speed
Q_loc = []; % ACTUAL hexbug motion path
vel = []; % ACTUAL hexbug velocity
Q_loc_meas = []; % the hexbug path extracted by the tracking algo

%% initize estimation variables
Q_loc_estimate = []; %  position estimate
vel_estimate = []; % velocity estimate
P_estimate = P;
predic_state = [];
predic_var = [];
r = 5; % r is the radius of the plotting circle
j=0:.01:2*pi; %to make the plotting circle


for t = S_frame:length(f_list)
    
    % load the image
    img_tmp = double(imread(f_list(t).name));
    img = img_tmp(:,:,1);
    % load the given tracking
    Q_loc_meas(:,t) = [ CM_idx(t,1); CM_idx(t,2)];
    
    %% do the kalman filter   
    
    % Predict next state of the Hexbug with the last state and predicted motion.
    Q_estimate = A * Q_estimate + B * u;
    predic_state = [predic_state; Q_estimate(1)] ;
    %predict next covariance
    P = A * P * A' + Ex;
    predic_var = [predic_var; P] ;
    % predicted Ninja measurement covariance
    % Kalman Gain
    K = P*C'*inv(C*P*C'+Ez);
    % Update the state estimate.
    if ~isnan(Q_loc_meas(:,t))
        Q_estimate = Q_estimate + K * (Q_loc_meas(:,t) - C * Q_estimate);
    end
    % update covariance estimation.
    P =  (eye(4)-K*C)*P;
    
    %% Store data
    Q_loc_estimate = [Q_loc_estimate; Q_estimate(1:2)];
    vel_estimate = [vel_estimate; Q_estimate(3:4)];
   
    %% plot the images with the  tracking
    imagesc(img);
    axis off
    colormap(gray);
    hold on;
    plot(r*sin(j)+Q_loc_meas(2,t),r*cos(j)+Q_loc_meas(1,t),'.g'); % the actual tracking
    plot(r*sin(j)+Q_estimate(2),r*cos(j)+Q_estimate(1),'.r'); % the kalman filtered tracking
    hold off
    pause(0.1)
end
