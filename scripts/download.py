
import sys
import os
import codecs
import urllib
import configparser
import re

from datetime import datetime,timedelta
from os import listdir
from os.path import isfile, join
from os.path import basename
from random import randint

sys.path.insert(0,"/home/doried/shamra/python/video")
sys.path.insert(0,"/home/doried/shamra/python/lib")

import fix
from bs4 import BeautifulSoup



config_file_path = "/home/doried/tdt/data/work1/config.ini"

def write_config(key,val):
	config = configparser.ConfigParser()
	config.read(config_file_path)
	
	config["SETTINGS"][key] = str(val)

	with open(config_file_path, 'w') as configfile: 
		config.write(configfile)

def read_config(key):
	config = configparser.ConfigParser()
	config.read(config_file_path)
	return config["SETTINGS"][key];


def get_lines_of_file(f):
	lines = []
	while 1<2:
		line = f.readline().strip()
		if len(line)==0:
			break;
		lines.append(line)
	return lines


def test_buffer_for_redundancies(buffer_file,done_file):

	buffer_lines = get_lines_of_file(buffer_file)

	done_lines = get_lines_of_file(done_file)
	
	done_folders = done_lines[0::3]
	done_titles = done_lines[1::3]
	done_urls = done_lines[2::3]
	
	done = zip(done_folders,done_titles,done_urls)

	intersection = []

	index = 0
	while index<len(buffer_lines):
		

		l1 = buffer_lines[index+0]
		l2 = buffer_lines[index+1]
		l3 = buffer_lines[index+2]

		index = index + 3

		for l in [l2,l3]:
			if l[0] != '#' :
				tmp = [x for x in done if l==x[2]]
				if len(tmp)>0:
					intersection.append(tmp[0])

	if len(intersection)==0:
		print 'Ok.. no intersection'
	else:
		print 'Intersections in line(s):'
		for intersect in intersection:
			print intersect
		print "==========================="

	return intersection


def get_number_of_lines(file_path):
	f = codecs.open(file_path,"r","utf-8")
	lines = get_lines_of_file(f)
	f.close()
	return len(lines)


def get_last_filename(dir):
	onlyfiles = [int(os.path.splitext(f)[0]) for f in listdir(dir) if f != "info.txt" and isfile(join(dir, f))]
	if len(onlyfiles)==0:
		return -1
	return max(onlyfiles)


def extract_date(date_str):

	date = datetime.now()

	try:
		m = re.match("\W*\D*(\d+) (hours|hour|second|seconds|minute|minutes)(.*)", date_str)
		num = int(m.group(1))
		unit = m.group(2)
		
		if unit in ['hour' , 'hours']:
			date = date + timedelta(hours=-num)
		elif unit in ['minute' , 'minutes']:
			date = date + timedelta(minutes=-num)
		elif unit in ['second' , 'seconds']:
			date = date + timedelta(seconds=-num)
	except:
		date = datetime.strptime(date_str, '%b %d, %Y')
		rand_hours = randint(0,23);
		date = date + timedelta(hours=rand_hours)

	print date_str + " : " + str(date)
	return date



def download_search_results_to_topic(topic_title, search_url, download_path):
	url = search_url


	if download_path[-1] != '/':
		download_path = download_path + '/';

	urls = []

	try:
		info_file = codecs.open(download_path + "info.txt" , "r" , "utf-8");
		urls = get_lines_of_file(info_file)
		urls = urls[0::3]
		info_file.close;
	except:
		print "Creating directory for topic";
		try:
			os.makedirs(download_path)
		except:
			print "Directory exists.Continuing..."

	info_file = codecs.open(download_path + "info.txt" , "a" , "utf-8");


	fix.fix(search_url,"tmp.txt")

	doc_file = codecs.open("tmp.txt", "r","utf-8")
	doc= doc_file.read()
	doc_file.close()

	print "Downloaded search results. Now parsing them..";

	soup = BeautifulSoup(doc,'html.parser')
	articles = soup.findAll("div", {"id" : "story-articles"})[0];
	titles = articles.findAll("h2" , {"class" : "title"})
	subtitles = articles.findAll("div" , {"class" : "sub-title"});	
	idx = -1

	c=get_last_filename(download_path)+1

	for title in titles:
		
		idx = idx+1

		a = title.findAll("a")[0];
		url = a["href"];

		if url in urls:
			print 'Url ' + url + ' exists. skipping..'
			continue


		titletext = title.findAll("span" , {"class" : "titletext"})[0].text.encode("utf-8")

		date = subtitles[idx].findAll("span" , {"class" : "date"})[0].text.strip();
		if not (date[0] >= '0' and date[0] <= '9'):
			date = date[1:]
		if not (date[-1] >= '0' and date[-1] <= '9'):
			date = date[0:-1]
		date_val = extract_date(date)


		file_name = str(download_path) + str(`c`) + ".html" ;
		print "Downloading " + url + " to file " + file_name; 
		fix.fix(url,file_name)

		info_file.write(url + "\n" + titletext.decode("utf-8") + "\n" + str(date_val) + "\n")
		c = c+1

	info_file.close();
	return 1<2


def trunc():
	tmp_file = codecs.open( "/home/doried/tdt/stats.txt" , "r" , "utf-8" )
	out_file = codecs.open( "/home/doried/tdt/tmp1.txt" , "w" , "utf-8" )


	while 1<2:
		line = tmp_file.readline().strip();
		if len(line)==0:
			break;
		line = line [7:]
		line = line.replace("www.","")
		line = line[0: line.index(".")];
		out_file.write(line + "\n")



def main():

	buffer_path = "/home/doried/tdt/data/work1/buffer.txt";
	todo_path = "/home/doried/tdt/data/work1/todo.txt"
	done_path = "/home/doried/tdt/data/work1/done.txt"
	download_path = "/home/doried/tdt/data/work1/downloaded/"
	infofile_path = "/home/doried/tdt/data/work1/info.txt"

	buffer_file = codecs.open(buffer_path , "r" , "utf-8")
	done_file   = codecs.open(done_path   , "r" , "utf-8")

	intersections = test_buffer_for_redundancies(buffer_file,done_file);
	isOk = (len(intersections) == 0)
	if not isOk :
		response = input("Intersections found, continue anyway? (y/n)");
		if response[0] in ['n' , 'N']:
			return;
		print 'I\'ll redownload the intersected urls and add only new documents to the topics';

	done_file.close();

	buffer_file.seek(0);
	buffer_lines = get_lines_of_file(buffer_file);
	buffer_file.close();

	#opening todo file
	todo_file   = codecs.open(todo_path   , "a" , "utf-8")

	topic_number = int(read_config("topics_count"))

	num_of_lines = len(buffer_lines)

	i=0
	while i<num_of_lines:
		l1 = buffer_lines[i]
		l2 = buffer_lines[i+1]
		l3 = buffer_lines[i+2]

		i = i+3

		topic_title = l1;

		new_topic = 0

		for l in [l2,l3]:
			if l[0]!='#':
				intersection = [ x for x in intersections if l == x[2] ]
				if len(intersection)==0:
					todo_file.write(`topic_number` + "\n" + topic_title + "\n" + l + "\n")
					new_topic = 1
				else:
					todo_file.write(intersection[0][0] +"\n"  + intersection[0][1]  + "\n" +  intersection[0][2] + "\n")
		
		if new_topic==1:
			topic_number = topic_number+1

	todo_file.close;

	write_config('topics_count',topic_number)

	#empting buffer file
	buffer_file = codecs.open(buffer_path , "w" , "utf-8")
	buffer_file.close();

  
	#opening done file for append
	done_file   = codecs.open(done_path   , "a" , "utf-8")
	
	#opening todo file for reading
	todo_file   = codecs.open(todo_path   , "r" , "utf-8")
	todo_lines = get_lines_of_file(todo_file)
	todo_file.close

	todos = []

	i = 0;
	len_todos = len(todo_lines)
	while i<len_todos:
		todos.append((todo_lines[i],todo_lines[i+1],todo_lines[i+2]))
		i = i + 3

	failed = []

	for todo in todos:
		topic_number = int(todo[0])
		topic_title  = todo[1]
		url 		 = todo[2]

		if not download_search_results_to_topic(topic_title, url , download_path + `topic_number`):
			failed.append(todo)
		else:
			done_file.write(`topic_number` + "\n" + `topic_title` + "\n" +  url + "\n")

	#empting todo file but writing only failed downloads
	todo_file = codecs.open(todo_path,"w","utf-8")

	for todo in failed:
		todo_file.write(todo[0] + "\n" + todo[1] + "\n" + todo[2] + "\n")

	todo_file.close;



main()
