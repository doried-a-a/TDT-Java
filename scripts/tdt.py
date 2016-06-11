
import sys
import os
import codecs
import urllib

sys.path.insert(0,"/home/doried/shamra/python/video")
sys.path.insert(0,"/home/doried/shamra/python/lib")

import fix
from bs4 import BeautifulSoup


def download_search_results(search_url , download_path,start_with,stats_file):
    url = search_url

    if download_path[-1] != '/':
        download_path = download_path + '/';
        
    
    fix.fix(url,"tmp.txt")

    doc_file = codecs.open("tmp.txt", "r","utf-8")
    doc= doc_file.read()
    doc_file.close()

    print "Downloaded search results. Now parsing them..";

    soup = BeautifulSoup(doc,'html.parser')
    articles = soup.findAll("div", {"id" : "story-articles"})[0];
    titles = articles.findAll("h2" , {"class" : "title"})
    
    c=start_with
    for title in titles:
        a = title.findAll("a")[0];
        url = a["href"];
        file_name = download_path + `c` + ".html" ;
        print "Downloading " + url + " to file " + file_name; 
        stats_file.write(url + "\n");
        fix.fix(url,file_name)
        c = c+1
        
    return c

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
    url = "https://news.google.com/news/story?cf=all&hl=en&ned=uk&ncl=dX5EMI9ZhmLMd4MzJeKzCshvFzRpM"
    path_to_download = "/home/doried/Desktop/";
    
    statistics_file = codecs.open("statistics.txt","w","utf-8");
    download_search_results(url,path_to_download,0,statistics_file)
    
main();

#    links_file_link = "/home/doried/tdt/links1.txt";
#    stats_file_link = "/home/doried/tdt/stats1.txt";
#
#    links_file = codecs.open( links_file_link , "r" , "utf-8" )
#    stats_file = codecs.open( stats_file_link , "w" , "utf-8" )
#
#    output_dir = "/home/doried/tdt"
#
#
#    if output_dir[-1] != '/':
#            output_dir = output_dir + '/';    
#
#    k = 9
#    while 1<2:
#
#        url = links_file.readline().strip();
#        if len(url)==0:
#            break;
#
#        if not os.path.exists(output_dir + `k`):
#            try:
#                os.makedirs(output_dir+`k`)
#            except OSError as exc: # Guard against race condition
#                if exc.errno != errno.EEXIST:
#                    raise
#
#        if len(url)>1:
#            res = download_search_results(url,output_dir+`k`,0,stats_file);
#        url = links_file.readline().strip();
#        if len(url)>1:    
#            res = download_search_results(url,output_dir+`k`,res,stats_file);
#        url = links_file.readline().strip();
#        #res = download_search_results(url,output_dir+`k`,res,stats_file);
#
#        k=k+1
#
#trunc();