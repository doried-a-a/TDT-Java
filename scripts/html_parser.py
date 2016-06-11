
import sys
import os
import codecs
import urllib
import operator

sys.path.insert(0, "/home/doried/shamra/python/lib")
from bs4 import BeautifulSoup

def get_text(doc, site_name):
	
	soup = BeautifulSoup(doc, 'html.parser')
	
	for script in soup(['style', 'script', '[document]', 'head', 'title']):
		script.extract()

	if site_name in [ 'wtvb','whtc', 'kdal-am', 'whbl' , 'wkzo' , 'kfgo' ]:
		body = soup.findAll("div", {"class": "article-body"})[0];
		ps = body.findAll("p")
		text = ""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
		return text
		
	elif site_name == 'SWI swissinfo.ch':
		section = soup.findAll("section", {"class": "articleContent main-content"})[0];
		ps = section.findAll("p", {"class": ""})
		text = ""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
		return text
		
	elif site_name == 'International Business Times UK':
		div = soup.findAll("div", {"class": "v_text" , "itemprop":"articleBody" , "id":"v_main"})[0];
		text = div.text.encode("utf-8")
		return text
	
	elif site_name == 'Middle East Monitor - The Latest from the Middle East':
		div = soup.findAll("div", {"class": "article-content"})[0];
		ps = div.findAll("p")
		text = ""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
		return text
		
	elif site_name == 'The Washingtion Times':
		div = soup.findAll("div", {"class": "storyareawrapper"})[0];
		ps = div.findAll("p")
		text = ""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
		return text
		
	elif site_name == 'The Hindu':
		ps = soup.findAll("p" , {"class":"body"})
		text = ""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
		return text
		
	elif site_name == 'The Indian Express':
		div = soup.findAll("div", {"itemprop":"articleBody" , "class":"full-details"})[0];
		ps = div.findAll("p")
		text = ""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
		return text
		
	elif site_name == 'The Sydney Morning Herald':
		div = soup.findAll("div", {"itemprop":"articleBody" , "class":"articleBody"})[0];
		ps = div.findAll("p")
		text = ""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
		return text
		
	elif site_name == 'BelfastTelegraph.co.uk':
		section = soup.findAll("section", {"class":"body"})[0];
		ps = section.findAll("p")
		text = ""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
		return text
	
	elif site_name == 'mirror':
		div = soup.findAll("div", {"itemprop":"articleBody" , "class":"body"})[0];
		ps = div.findAll("p",{"class":""})
		text = ""
		for p in ps:
			for span in p(['div']):
				span.extract()
			a = p.text
			text = text + "\n" + a.encode("utf-8")
		return text
	
	elif site_name == 'The Irish Times':
		div = soup.findAll("div", {"class":"article_bodycopy"})[0];
		ps = div.findAll("p")
		text = ""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
		return text
		
	elif site_name == '###':
		div = soup.findAll("div", {"itemprop":"articleBody" , "class":"full-details"})[0];
		ps = div.findAll("p")
		text = ""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
		print "Ok for " + site_name ; print text;
		return text
		
	elif site_name == '###':
		div = soup.findAll("div", {"itemprop":"articleBody" , "class":"full-details"})[0];
		ps = div.findAll("p")
		text = ""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
		print "Ok for " + site_name ; print text;
		return text
		
	elif site_name == "Mail Online":
		section = soup.findAll("div", {"itemprop": "articleBody"})[0];
		ps = section.findAll("p", {"class": ""})
		text = ""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
		return text
	elif site_name == "Houston Chronicle":
		section = soup.findAll("div", {"class": "article-body"})[0];
		ps = section.findAll("p")	
		text = ""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
		return text
	elif site_name == "BBC News":
		section = soup.findAll("div", {"class": "story-body__inner"})[0];
		ps = section.findAll("p")
		text = ""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
		return text
	elif site_name == "The New Zealand Herald":
		section = soup.findAll("div", {"class": "articleBody"})[0];
		ps = section.findAll("p")
	
		text = ""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
	   
		return text
	elif site_name == "Business Recorder":
		section = ""
		try:
			section = soup.findAll("article", {"class": "article"})[0];
		except:
			section = soup.findAll("div", {"class": "article"})[0];

		ps = section.findAll("p")
	
		text = ""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
	   
		return text

	elif site_name == "RT International":
		section = soup.findAll("div", {"class": "article__text text "})[0];
		ps = section.findAll("p")
	
		text = ""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
	   
		return text
	elif site_name == "VOA":
		section = soup.findAll("div", {"class": "zoomMe"})[0];
		ps = section.findAll("p")
	
		text = ""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
	   
		return text

	elif site_name == "Al Bawaba":
		section = soup.findAll("div", {"class": "content clearfix"})[0];
		ps1 = section.findAll("span", {"style": "background-color: transparent;"})
		ps = section.findAll("p", {"class": ""})
		section1 = soup.findAll("div", {"class": "teads-inread"})[6];
		ps2 = section1.findAll("p", {"class": ""})
		text = ""
		x = 0
		for p in ps:
			x = x + 1
			if x == 3:
				break;  
			a = p.text
			text = text + "\n" + a.encode("utf-8")
		for p in ps1:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
		for p in ps2:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
			return text
	
	elif site_name == "euronews":
		section = soup.findAll("div", {"class": "noembed"})[0];
		ps = section.findAll("p")
		text = ""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
		return text

	elif site_name == "The Times":
		section = soup.findAll("div", {"class": "contentpage currentpage"})[0];
		ps = section.findAll("p")
		text = ""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
		return text

	elif site_name == "The New Yorker":
		section = soup.findAll("div", {"itemprop": "articleBody"})[0];
		ps = section.findAll("p")
		text = ""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
		return text
	elif site_name == "stltoday.com":
		section = soup.findAll("div", {"class": "entry-content"})[0];
		ps = section.findAll("p")
		text = ""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
		return text

	elif site_name == "The Times of India":
		section = soup.findAll("div", {"class": "section1"})[0];
		ps = section.findAll("div", {"class": "Normal"})
		text = ""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
		return text

	elif site_name == "Independent.ie":
		section = soup.findAll("section", {"class": "body"})[0];
		ps = section.findAll("p")
		text = ""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
		return text
	
	elif site_name == "South China Morning Post":
		section = soup.findAll("div", {"class": "panel-pane pane-entity-field pane-node-body pos-1"})[0];
		ps = section.findAll("p", {"class": ""})
		text = ""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
		return text

	elif site_name == "the Guardian":
		section = soup.findAll("div", {"itemprop": "articleBody"})[0];
		ps = section.findAll("p")
		text = ""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
		return text

	elif site_name == "NOLA.com":
		section = soup.findAll("div", {"class": "entry-content"})[0];
		ps = section.findAll("p", {"class": ""})
		text = ""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
		return text

	elif site_name == "The Nation":
		section = soup.findAll("div", {"class":"post-content" , "itemprop": "description"})[0];
		ps = section.findAll("p")
		text = ""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
		return text

	elif site_name == "The Huffington Post":
		section = soup.findAll("div", {"class": "entry__body js-entry-body"})[0];
		ps = section.findAll("p", {"class": ""})	
		text = ""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
		return text

	elif site_name == "Financial Review":
		section = soup.findAll("div", {"class": "article__content"})[0];
		ps = section.findAll("p", {"class": ""})
		text = ""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
		return text

	elif site_name == "The Salt Lake Tribune":
		section = soup.findAll("div", {"itemprop": "articleBody"})[0];
		ps = section.findAll("p", {"class": "TEXT_w_Indent"})
		section1 = soup.findAll("div", {"class": "col-md-8 col-lg-9 content-area content-full-article pull-right"})[0];
		ps1 = section1.findAll("p", {"class": "TEXT_w_Indent"})
		text = ""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
		for p in ps1:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
		return text

	elif site_name == "The Independent":
		section = soup.findAll("div", {"itemprop": "articleBody"})[0];
		ps = section.findAll("p", {"class": ""})
		text = ""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
		return text

	elif site_name == "DailyTimes":
		section = soup.findAll("div", {"itemprop": "articleBody"})[0];
		ps = section.findAll("p", {"class": ""})	
		text = ""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
		return text
	
	elif site_name == "Reuters UK":
		section = soup.findAll("span", {"id": "articleText"})[0];
		ps = section.findAll("p", {"class": ""})
		text = ""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
		return text

	elif site_name == "The Australian":
		section = soup.findAll("div", {"id": "content-2"})[0];
		ps = section.findAll("p", {"class": ""})
		ps1 = section.findAll("ol", {"class": ""})
		text = ""
		x = 0
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
		
			a = ps1[x].text
			text = text + "\n" + a.encode("utf-8")
			x = x + 1
	   
		return text
	elif site_name == "Telegraph.co.uk":
		section = soup.findAll("div", {"class": "story"})[0];
		ps = section.findAll("p", {"class": ""})
		text = ""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
		return text

	elif site_name == "NBC News":
		section = soup.findAll("div", {"itemprop": "articleBody"})[0];
		ps = section.findAll("p", {"class": ""})	
		text = ""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
	   
		return text

	elif site_name == "RadioFreeEurope/RadioLiberty":
		
		section = soup.findAll("div", {"class": "articleContent"})[0];
		ps = section.findAll("p", {"class": ""})

		text = ""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
	   
		return text
	elif site_name == "NY Daily News":
		
		section = soup.findAll("article", {"itemprop": "articleBody"})[0];
		ps = section.findAll("p", {"class": ""})

		text = ""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
	   
		return text

	elif site_name == "CNN":
		
		section = soup.findAll("section",{"id": "body-text"} )[0];
		
		ps = section.findAll("p", {"class": "zn-body__paragraph"})

		text = ""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
		
		return text
	elif site_name == "NDTV.com":
		
		section = soup.findAll("div", {"itemprop": "articleBody"})[0];
		text = ""
		a = section.text
		text = text + "\n" + a.encode("utf-8")
	   
		return text

	elif site_name == "http://www.hindustantimes.com/":
		
		section = soup.findAll("div", {"class": "sty_txt"})[0];
		ps = section.findAll("p", {"class": ""})

		text = ""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
	   
		return text
	elif site_name == "CNBC":
		
		section = soup.findAll("div", {"itemprop": "articleBody"})[0];
		ps = section.findAll("p", {"class": ""})

		text = ""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
	   
		return text

	elif site_name == "Washington Post":
		
		section = soup.findAll("article", {"itemprop": "articleBody"})[0];
		ps = section.findAll("p", {"class": ""})

		text = ""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
	   
		return text

	elif site_name == "TODAYonline":
		
		section = soup.findAll("div", {"class": "content"})[0];
		ps = section.findAll("p", {"class": ""})

		text = ""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
	   
		return text
	elif site_name == "The Straits Times":
		
		section = soup.findAll("div", {"itemprop": "articleBody"})[0];
		ps = section.findAll("p", {"class": ""})

		text = ""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
	   
		return text

	elif site_name == "Reuters":
		
		section = soup.findAll("span", {"id": "articleText"})[0];
		ps = section.findAll("p", {"class": ""})

		text = ""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
	   
		return text

	elif site_name == "Channel NewsAsia":
		
		section = soup.findAll("div", {"class": "news_detail"})[0];
		ps = section.findAll("p", {"class": ""})

		text = ""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
	   
		return text
	elif site_name == "USA TODAY":
		
		section = soup.findAll("div", {"itemprop": "articleBody"})[0];
		ps = section.findAll("p", {"class": ""})

		text = ""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
	   
		return text
	elif site_name == "NewsComAu":
		
		section = soup.findAll("div", {"class": "story-body"})[0];
		ps = section.findAll("p", {"class": ""})

		text = ""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
	   
		return text
	elif site_name == "Sky News":
		
		section = soup.findAll("div", {"class": "content-column"})[0];
		ps = section.findAll("p", {"class": "story__intro"})
		ps1 = section.findAll("p", {"class": ""})

		text = ""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
	   
	
		for p in ps1:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
	   
		return text

	elif site_name == "Zee News":
		
		section = soup.findAll("div", {"class": "field field-name-body field-type-text-with-summary field-label-hidden"})[0];
		ps = section.findAll("p", {"class": ""})

		text = ""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
	   
		return text

	elif site_name == "NPR.org":
		
		section = soup.findAll("div", {"class": "storytext storylocation linkLocation"})[0];
		ps = section.findAll("p", {"class": ""})

		text = ""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
	   
		return text

	elif site_name == "Here Is The City":
		section = soup.findAll("section", {"class": "post-content"})[0];
		ps = section.findAll("p", {"class": ""})

		text = ""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
	   
		return text

	elif site_name == "South Wales Evening Post":
		section = soup.findAll("div", {"class": "story-body norestrictImageSharing"})[0];
		ps = section.findAll("p", {"class": ""})

		text = ""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
	   
		return text

	elif site_name == "Crewe Guardian":
		section = soup.findAll("div", {"class": "grid_8 alpha story-details small"})[0];
		ps = section.findAll("p", {"class": ""})

		text = ""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
	   
		return text

	elif site_name == "News Shopper":
		section = soup.findAll("div", {"class": "grid_8 alpha story-details small"})[0];
		ps = section.findAll("p", {"class": ""})

		text = ""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
	   
		return text
	elif site_name == "South Wales Argus":
		section = soup.findAll("div", {"class": "grid_8 alpha story-details small"})[0];
		ps = section.findAll("p", {"class": ""})

		text = ""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
	   
		return text
	elif site_name == "Runcorn and Widnes World":
		section = soup.findAll("div", {"class": "grid_8 alpha story-details small"})[0];
		ps = section.findAll("p", {"class": ""})

		text = ""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
	   
		return text
	elif site_name == "Chester-le-Street Advertiser":
		section = soup.findAll("div", {"class": "grid_8 alpha story-details small"})[0];
		ps = section.findAll("p", {"class": ""})

		text = ""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
	   
		return text

	elif site_name == "Messenger Newspapers":
		section = soup.findAll("div", {"class": "grid_8 alpha story-details small"})[0];
		ps = section.findAll("p", {"class": ""})

		text = ""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
	   
		return text
	elif site_name == "Cotswold Journal":
		section = soup.findAll("div", {"class": "grid_8 alpha story-details small"})[0];
		ps = section.findAll("p", {"class": ""})

		text = ""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
	   
		return text
	elif site_name == "Express.co.uk":
		section = soup.findAll("article", {"itemprop": "mainContentOfPage"})[0];		
		ps = section.findAll("p", {"class": ""})

		text = ""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
	   
		return text

	elif site_name == "Basingstoke Gazette":
		section = soup.findAll("div", {"class": "grid_8 alpha story-details small"})[0];
		ps = section.findAll("p", {"class": ""})

		text = ""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
	   
		return text

	elif site_name == "Warrington Guardian":
		section = soup.findAll("div", {"class": "grid_8 alpha story-details small"})[0];
		ps = section.findAll("p", {"class": ""})

		text = ""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
	   
		return text

	elif site_name == "Barry And District News":
		section = soup.findAll("div", {"class": "grid_8 alpha story-details small"})[0];
		ps = section.findAll("p", {"class": ""})

		text = ""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
	   
		return text

	elif site_name == "Irvine Times":
		section = soup.findAll("div", {"class": "grid_8 alpha story-details small"})[0];
		ps = section.findAll("p", {"class": ""})

		text = ""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
	   
		return text
	elif site_name == "Clacton and Frinton Gazette":
		section = soup.findAll("div", {"class": "grid_8 alpha story-details small"})[0];
		ps = section.findAll("p", {"class": ""})

		text = ""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
	   
		return text

	elif site_name == "Gazette & Herald":
		section = soup.findAll("div", {"class": "grid_8 alpha story-details small"})[0];
		ps = section.findAll("p", {"class": ""})

		text = ""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
	   
		return text
	elif site_name == "Southend Standard":
		section = soup.findAll("div", {"class": "grid_8 alpha story-details small"})[0];
		ps = section.findAll("p", {"class": ""})

		text = ""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
	   
		return text

	elif site_name == "Stourbridge News":
		section = soup.findAll("div", {"class": "grid_8 alpha story-details small"})[0];
		ps = section.findAll("p", {"class": ""})

		text = ""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
	   
		return text
	elif site_name == "Somerset County Gazette":
		section = soup.findAll("div", {"class": "grid_8 alpha story-details small"})[0];
		ps = section.findAll("p", {"class": ""})

		text = ""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
	   
		return text

	elif site_name == "http://www.newsnation.in":
		section = soup.findAll("div", {"itemprop": "articleBody"})[0];
		ps = section.findAll("p", {"class": ""})

		text = ""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
	   
		return text

	elif site_name == "GlobalPost":
		section = soup.findAll("div", {"itemprop": "articleBody"})[0];
		ps = section.findAll("p", {"class": ""})

		text = ""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
	   
		return text
	elif site_name == "Bromsgrove Advertiser":
		section = soup.findAll("div", {"class": "grid_8 alpha story-details small"})[0];
		ps = section.findAll("p", {"class": ""})

		text = ""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
	   
		return text

	elif site_name == "Hampshire Chronicle":
		section = soup.findAll("div", {"class": "grid_8 alpha story-details small"})[0];
		ps = section.findAll("p", {"class": ""})

		text = ""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
	   
		return text

	elif site_name == "Northwich Guardian":
		section = soup.findAll("div", {"class": "grid_8 alpha story-details small"})[0];
		ps = section.findAll("p", {"class": ""})

		text = ""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
	   
		return text

	elif site_name == "Your Local Guardian":
		section = soup.findAll("div", {"class": "grid_8 alpha story-details small"})[0];
		ps = section.findAll("p", {"class": ""})

		text = ""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
	   
		return text

	
	elif site_name == "Dumbarton and Vale of Leven Reporter":
		section = soup.findAll("div", {"class": "grid_8 alpha story-details small"})[0];
		ps = section.findAll("p", {"class": ""})

		text = ""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
	   
		return text

	elif site_name == "Free Press Series":
		section = soup.findAll("div", {"class": "grid_8 alpha story-details small"})[0];
		ps = section.findAll("p", {"class": ""})

		text = ""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
	   
		return text
	#######################################################################3##############################
	elif site_name == "Bloomberg.com":
		section = soup.findAll("div", {"class": "article-body__content"})[0];
		ps = section.findAll("p")

		text = ""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
	   
		return text
	
		
	elif site_name=="The Irish News":
		
		section = soup.findAll("div", {"class" : "lancio-text"})[0];
		ps = section.findAll("p")

		text=""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
	   
		return text
		
	elif site_name=="Daily Echo":
		
		section = soup.findAll("div", {"class" : "grid_8 alpha story-details small"})[0];
		ps = section.findAll("p")

		text=""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
	   
		return text
		
	elif site_name=="Echo":
		
		section = soup.findAll("div", {"class" : "grid_8 alpha story-details small"})[0];
		ps = section.findAll("p")

		text=""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
	   
		return text
		
	elif site_name=="The Huffington Post UK":
		
		section = soup.findAll("article", {"class" : "entry"})[0];
		ps = section.findAll("p")

		text=""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
	   
		return text
		
	elif site_name=="Herald Scotland":
		
		section = soup.findAll("div", {"class" : "grid_8 alpha story-details small"})[0];
		ps = section.findAll("p")

		text=""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
	   
		return text
		
	elif site_name=="AOL News UK":
		
		section = soup.findAll("div", {"class" : "article-pa"})[0];
		ps = section.findAll("p")

		text=""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
	   
		return text
	elif site_name=="Shropshire Star":
		
		section = soup.findAll("div", {"class" : "content contentContainer"})[0];
		ps = section.findAll("p")

		text=""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
	   
		return text
		
	elif site_name=="Express &amp; Star":
		
		section = soup.findAll("div", {"class" : "content contentContainer"})[0];
		ps = section.findAll("p")

		text=""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
	   
		return text
		
	elif site_name=="BT.com":
		
		section = soup.findAll("div", {"itemprop" : "articleBody"})[0];
		ps = section.findAll("p")

		text=""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
	   
		return text
		
	elif site_name=="Metro":
		
		section = soup.findAll("div", {"class" : "article-body"})[0];
		ps = section.findAll("p" , {"class"  : ""})

		text=""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
	   
		return text
		
	elif site_name=="getwestlondon":
		
		section = soup.findAll("div", {"itemprop" : "articleBody"})[0];
		ps = section.findAll("p")

		text=""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
	   
		return text
	elif site_name=="Plymouth Herald":
		
		section = soup.findAll("div", {"class" : "story-body norestrictImageSharing"})[0];
		ps = section.findAll("p")

		text=""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
	   
		return text
		
	elif site_name=="Bath Chronicle":
		
		section = soup.findAll("div", {"class" : "story-body norestrictImageSharing"})[0];
		ps = section.findAll("p")

		text=""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
	   
		return text
	elif site_name=="Dailystar.co.uk":
		
		div = soup.findAll("div", {"class" : "story-content  p402_premium clearfix"})[0];
		sections = div.findAll("section" , {"class"  : "text-description"})

		text=""
		for sec in sections:
			ps1 = sec.findAll("p")
			for p in ps1:
				a = p.text
				text = text + "\n" + a.encode("utf-8")
		return text

	elif site_name=="Exeter Express and Echo":
		
		section = soup.findAll("div", {"class" : "story-body norestrictImageSharing"})[0];
		ps = section.findAll("p")

		text=""
		for p in ps:
			
		   
				a = p.text
				text = text + "\n" + a.encode("utf-8")
		return text 
	elif site_name=="mirror":
		for script in soup(['button','h3','figure','span']):
			script.extract()
	
		section = soup.findAll("div", {"itemprop" : "articleBody"})[0];
		ps = section.findAll("p")

		text=""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
	   
		return text
		
	elif site_name=="Bristol Post":
		
		section = soup.findAll("div", {"class" : "story-body norestrictImageSharing"})[0];
		ps = section.findAll("p")

		text=""
		for p in ps:
			a = p.text
			text = text + "\n" + a.encode("utf-8")
		return text
		
	elif site_name=="The Indian Express":
		
		section = soup.findAll("div", {"itemprop" : "articleBody"})[0];
		ps = section.findAll("p")

		text=""
		for p in ps:
				a = p.text
				text = text + "\n" + a.encode("utf-8")
		return text
		
	elif site_name=="CBSSports.com":
		
		section = soup.findAll("div", {"class" : "storyCopy"})[0];
		ps = section.findAll("p")

		text=""
		for p in ps:
				a = p.text
				text = text + "\n" + a.encode("utf-8")
		return text

	elif site_name=="TSN":
		
		section = soup.findAll("div", {"class" : "article-text"})[0];
		ps = section.findAll("p")

		text=""
		for p in ps:
				a = p.text
				text = text + "\n" + a.encode("utf-8")
		return text
	
	elif site_name=="http://www.bangkokpost.com":
		
		section = soup.findAll("div", {"class" : "articleContents"})[0];
		ps = section.findAll("p")

		text=""
		for p in ps:
				a = p.text
				text = text + "\n" + a.encode("utf-8")
		return text
	elif site_name=="Goal.com":
		
		section = soup.findAll("div", {"itemprop" : "articleBody"})[0];
		ps = section.findAll("p" , {"class"  : "p1"})
		text=""
		if ps==[]:
			text=section.text.encode("utf-8")
			return text
	
		else:
			ps=section.findAll("p",{"class":""})
			for p in ps :
				a=p.text
				text = text + "\n" + a.encode("utf-8")
			return text				   

		for p in ps:
				a = p.text
				text = text + "\n" + a.encode("utf-8")
		return text
		
		
	elif site_name=="ESPN.com":
		
		section = soup.findAll("div", {"class" : "article-body"})[0];
		ps = section.findAll("p")

		text=""
		for p in ps:
			
		   
				a = p.text
				text = text + "\n" + a.encode("utf-8")
		return text

	elif site_name=="Bleacher Report":
		
		section = soup.findAll("div", {"itemprop" : "articleBody"})[0];
		ps = section.findAll("p")

		text=""
		for p in ps:
			
		   
				a = p.text
				text = text + "\n" + a.encode("utf-8")
		return text

	elif site_name=="Firstpost":
		
		section = soup.findAll("div", {"class" : "fullCont1"})[0];
		ps = section.findAll("p")

		text=""
		for p in ps:
			
		   
				a = p.text
				text = text + "\n" + a.encode("utf-8")
		return text

	elif site_name=="Stuff":
		
		section = soup.findAll("article", {"class" : "story_landing"})[0];
		ps = section.findAll("p")

		text=""
		for p in ps:
			
		   
				a = p.text
				text = text + "\n" + a.encode("utf-8")
		return text

	elif site_name=="Standard Digital News":
		
		section = soup.findAll("div", {"class" : "story"})[0];
		ps = section.findAll("p")

		text=""
		for p in ps:
				a = p.text
				text = text + "\n" + a.encode("utf-8")
		return text

	else:
		return 1==2

   
#dailystar(4times in d6) metro(6times in 6) itv(5 in 8)  huffingtonpost (7times in d5,6)  bristolpost(2t in d6) 
#cbssports(2t in d7)  bleacherreport (3t in d7) Goal.com (9t in d 7) firstpost(5t in d7) dailyrecord(3 in 8)
#radiotoday(2 in 8) marketwatch(2 in 9) thedailybeast(2 in 9) philly.com (3 in 9) news24(2 in 9) timeslive(2 in 10)
#The Daily Star Newspaper - Lebanon (in d10) 

sites = dict()

def main():
	
	no_meta_count = 0
	for folder in range(0,42):
		index = str(folder)
		input_path = "/home/doried/tdt/data/test2/" + index + "/"
		output_path = "/home/doried/tdt/data/test2/text/" + index + "/"
		
		if not os.path.exists(output_path):
			try:
				os.makedirs(output_path)
			except OSError as exc: # Guard against race condition
				if exc.errno != errno.EEXIST:
					raise

		list = os.listdir(input_path);
		files = [os.path.join(input_path, f) for f in list if f != "info.txt" and os.path.isfile(os.path.join(input_path, f))]
		
		info_file_path = os.path.join(input_path, "info.txt")
		info_file = codecs.open(info_file_path,"r","utf-8");
		info = [] #url , title , date

		while 1<2:
			url = info_file.readline().strip();
			if len(url)==0:
				break;
			title = info_file.readline().strip();
			date = info_file.readline().strip();
			info.append((url,title,date));

		for path in files:
			fi = codecs.open(path, "r");
			doc = fi.read()
			fi.close();

			filename = os.path.basename(path);
			filename_no_extension = os.path.splitext(filename)[0]

			file_info = info[int(filename_no_extension)]

			try:
				soup = BeautifulSoup(doc, 'html.parser')

				s_name = "";
				try:
					tmp = soup.find("meta", {"property": "og:site_name"});
					site_name = tmp["content"]
					s_name = site_name;
				except:
					no_meta_count += 1
					print "og:site_name meta not found for file " + path
				
				#try:
				#	tmp = soup.findAll("meta", {"property" : "og:url"})[0];
				#	url = tmp["content"]
				#except:
				#	print "og:url meta not found."

				if len(s_name) > 0:
					text = get_text(doc, site_name);
					if text != (1==2):
						filename = os.path.basename(path);
						output_file = codecs.open(os.path.join(output_path, filename) + ".txt", "w","utf-8");
						output_file.write(file_info[0] + "\n" + file_info[1] + "\n" + file_info[2] + "\n");
						output_file.write(text.decode("utf-8"));
						output_file.close();
					else:
						print "Site '" + s_name + "' is not known."
						if s_name in sites:
							sites[s_name] += 1
						else:
							sites[s_name] = 1
					
			except Exception, e:
				print 'Error in file ' + path;
				print "Error Message " + str(e);
	
	sorted_x = sorted(sites.items(), key=operator.itemgetter(1))
	print "------------------------";
	for x in sorted_x[-1:0:-1]:
		print x[0].encode("utf-8") + " : " + `x[1]`;
	print sorted_x

	print `no_meta_count`


main();
