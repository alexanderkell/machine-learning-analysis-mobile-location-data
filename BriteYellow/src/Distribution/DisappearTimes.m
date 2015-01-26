%% Number of times disappeared from the tracking system

labels = {'1-2' '2-3' '3-4' '4-5' '5-6' '6-7' '7-8' '8-9' '9-10' '10-11' '11-12' '12-13' '13-14' '14-15' '15-16' '>16'};

% Phone 1 to Phone 5
% 24/09/2014

pro = [0.42857143 0.2857143 0.14285715 0.0 0.0 0.14285715 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 9.519458E-4;
    0.6666667 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.16666667 0.0 0.0012936592;
    0.54545456 0.0 0.09090909 0.0 0.18181819 0.09090909 0.09090909 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 7.3581934E-4;
    0.6 0.0 0.0 0.0 0.19999999 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.19999999 0.004098356
    0.5652174 0.2173913 0.04347826 0.04347826 0.04347826 0.04347826 0.0 0.0 0.0 0.0 0.0 0.0 0.0 0.04347826 0.0 5.001426E-4;
    ];

figure
        bar(transpose(pro))
        xlabel('Inactive time(min)')
        ylabel('% of total number of times inactive for more than 1 min')
        title('Number of times inactive for more than 1 min 24/09/2014')
        legend('Phone 1: Business','Phone 2: Security', 'Phone 3: Shopper', 'Phone 4: Shopper','Phone 5: Business')
        set(gca, 'XTick', 1:16, 'XTickLabel', labels);


% Phone 1 to Phone 5
% 26/09/2014

pro = [0.5555556 0.0 0.11111112 0.11111112 0.0 0.11111112 0.0 0.0 0.0 0.0 0.11111112 0.0 0.0 0.0 0.0 2.872944E-4;
    0.33333337 0.0 0.0 0.16666669 0.0 0.16666669 0.16666669 0.0 0.0 0.16666669 0.0 0.0 0.0 0.0 0.0 7.572174E-4;
    0.625 0.0 0.0 0.20833333 0.0 0.041666664 0.041666664 0.0 0.0 0.0 0.0 0.0 0.08333333 0.0 0.0 5.232692E-4
    0.44444445 0.22222222 0.0 0.22222222 0.0 0.0 0.0 0.0 0.11111111 0.0 0.0 0.0 0.0 0.0 0.0 0.0
    0.6 0.13333334 0.13333334 0.0 0.0 0.06666667 0.0 0.0 0.0 0.0 0.06666667 0.0 0.0 0.0 0.0 7.3343515E-4
    ];

figure
        bar(transpose(pro))
        xlabel('Inactive time(min)')
        ylabel('% of total number of times inactive for more than 1 min')
        title('Number of times inactive for more than 1 min 26/09/2014')
        legend('Phone 1: Business','Phone 2: Security', 'Phone 3: Shopper', 'Phone 4: Shopper','Phone 5: Business')
        set(gca, 'XTick', 1:16, 'XTickLabel', labels);