#!/usr/bin/python
# -*- coding: utf-8 -*-
#
#== Motrice Copyright Notice == 
#  
# Motrice Service Platform 
#  
# Copyright (C) 2011-2014 Motrice AB 
#  
# This program is free software: you can redistribute it and/or modify 
# it under the terms of the GNU Affero General Public License as published by 
# the Free Software Foundation, either version 3 of the License, or 
# (at your option) any later version. 
# 
# This program is distributed in the hope that it will be useful, 
# but WITHOUT ANY WARRANTY; without even the implied warranty of 
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
# GNU Affero General Public License for more details. 
#  
# You should have received a copy of the GNU Affero General Public License 
# along with this program. If not, see <http://www.gnu.org/licenses/>. 
#  
# e-mail: info _at_ motrice.se 
# mail: Motrice AB, Långsjövägen 8, SE-131 33 NACKA, SWEDEN 
# phone: +46 8 641 64 14 

 
import pylab
import numpy as na
import matplotlib.font_manager
import csv
import sys
import os
 
elapsed = {}
timestamps = {}
starttimes = {}
errors = {}
 
# Parse the CSV files
for file in sys.argv[1:]:
  filename=os.path.basename(file) 
  dirname=os.path.dirname(file) 
  print filename 
  threads = int(filename.split('_')[0])
  for row in csv.DictReader(open(file)):
    print row
    if (not row['label'] in elapsed):
      elapsed[row['label']] = {}
      timestamps[row['label']] = {}
      starttimes[row['label']] = {}
      errors[row['label']] = {}
    if (not threads in elapsed[row['label']]):
      elapsed[row['label']][threads] = []
      timestamps[row['label']][threads] = []
      starttimes[row['label']][threads] = []
      errors[row['label']][threads] = []
    elapsed[row['label']][threads].append(int(row['elapsed']))
    timestamps[row['label']][threads].append(int(row['timeStamp']))
    starttimes[row['label']][threads].append(int(row['timeStamp']) - int(row['elapsed']))
    if (row['success'] != 'true'):
      errors[row['label']][threads].append(int(row['elapsed']))
 
# Draw a separate figure for each label found in the results.
for label in elapsed:
  # Transform the lists for plotting
  plot_data = []
  throughput_data = [None]
  error_x = []
  error_y = []
  plot_labels = []
  column = 1
  for thread_count in pylab.sort(elapsed[label].keys()):
    plot_data.append(elapsed[label][thread_count])
    plot_labels.append(thread_count)
    test_start = min(starttimes[label][thread_count])
    test_end = max(timestamps[label][thread_count])
    test_length = (test_end - test_start) / 1000
    num_requests = len(timestamps[label][thread_count]) - len(errors[label][thread_count])
    if (test_length > 0):
      throughput_data.append(num_requests / float(test_length))
    else:
      throughput_data.append(0)
    for error in errors[label][thread_count]:
      error_x.append(column)
      error_y.append(error)
    column += 1
 
 
  # Start a new figure
  fig = pylab.figure(figsize=(9, 6))
 
  # Pick some colors
  palegreen = matplotlib.colors.colorConverter.to_rgb('#8CFF6F')
  paleblue = matplotlib.colors.colorConverter.to_rgb('#708DFF')
 
  # Plot response time
  ax1 = fig.add_subplot(111)
  ax1.set_yscale('log')
  bp = pylab.boxplot(plot_data, notch=0, sym='+', vert=1, whis=1.5)
 
  # Tweak colors on the boxplot
  pylab.plt.setp(bp['boxes'], color='g')
  pylab.plt.setp(bp['whiskers'], color='g')
  pylab.plt.setp(bp['medians'], color='black')
  pylab.plt.setp(bp['fliers'], color=palegreen, marker='+')
 
  # Now fill the boxes with desired colors
  numBoxes = len(plot_data)
  medians = range(numBoxes)
  for i in range(numBoxes):
    box = bp['boxes'][i]
    boxX = []
    boxY = []
    for j in range(5):
      boxX.append(box.get_xdata()[j])
      boxY.append(box.get_ydata()[j])
    boxCoords = zip(boxX,boxY)
    boxPolygon = pylab.Polygon(boxCoords, facecolor=palegreen)
    ax1.add_patch(boxPolygon)
 
  # Plot the errors
  if (len(error_x) > 0):
    ax1.scatter(error_x, error_y, color='r', marker='x', zorder=3)
 
  # Plot throughput
  ax2 = ax1.twinx()
  ax2.plot(throughput_data, 'o-', color=paleblue, linewidth=2, markersize=8)
 
  # Label the axis
  ax1.set_title(label)
  ax1.set_xlabel('Number of concurrent requests')
  ax2.set_ylabel('Requests per second')
  ax1.set_ylabel('Milliseconds')
  ax1.set_xticks(range(1, len(plot_labels) + 1, 2))
  ax1.set_xticklabels(plot_labels[0::2])
  fig.subplots_adjust(top=0.9, bottom=0.15, right=0.85, left=0.15)
 
  # Turn off scientific notation for Y axis
  ax1.yaxis.set_major_formatter(pylab.ScalarFormatter(False))
 
  # Set the lower y limit to the match the first column
  ax1.set_ylim(ymin=bp['boxes'][0].get_ydata()[0])
 
  # Draw some tick lines
  ax1.yaxis.grid(True, linestyle='-', which='major', color='grey')
  ax1.yaxis.grid(True, linestyle='-', which='minor', color='lightgrey')
  # Hide these grid behind plot objects
  ax1.set_axisbelow(True)
 
  # Add a legend
  line1 = pylab.Line2D([], [], marker='s', color=palegreen, markersize=10, linewidth=0)
  line2 = pylab.Line2D([], [], marker='o', color=paleblue, markersize=8, linewidth=2)
  line3 = pylab.Line2D([], [], marker='x', color='r', linewidth=0, markeredgewidth=2)
  prop = matplotlib.font_manager.FontProperties(size='small')
  pylab.figlegend((line1, line2, line3), ('Response Time', 'Throughput', 'Failures (50x)'),
    'lower center', prop=prop, ncol=3)
 
  # Write the PNG file
  label= label.replace("/", "_").replace(".", "Dot")  ; 
  label = dirname + "/" + label 
  pylab.savefig(label) 
