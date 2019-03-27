import pandas as pd

df = pd.read_csv('HistoricalQuotes.csv')
change = 0
for i in range(0, 5):
    change = df['close'][i] - df['close'][i+1]
print(change)