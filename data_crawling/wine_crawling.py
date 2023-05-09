from selenium import webdriver
from bs4 import BeautifulSoup
from tkinter import *
from tkinter.ttk import *
import pandas as pd
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.common.exceptions import NoSuchElementException, TimeoutException
from selenium.webdriver.chrome.service import Service
from webdriver_manager.chrome import ChromeDriverManager
import re
import json
import tkinter as tk
from tkinter import ttk
from selenium import webdriver
from selenium.webdriver.chrome.options import Options


def extract_wine_information(driver):
    try:
        wine_name = driver.find_element(By.CSS_SELECTOR, "body > div.wrapper > div.container.content-xs > div.row > div.col-md-9.margin-bottom-10 > div > div.row > div.col-md-9 > div > h4 > strong ").text.split("\n")[1]
        wine_vintage = driver.find_element(By.CSS_SELECTOR, "body > div.wrapper > div.container.content-xs > div.row > div.col-md-9.margin-bottom-10 > div > div.row > div.col-md-9 > div > ul > li:nth-child(1)").text.split(" ")[1]
        wine_country = driver.find_element(By.CSS_SELECTOR, "body > div.wrapper > div.container.content-xs > div.row > div.col-md-9.margin-bottom-10 > div > div.row > div.col-md-9 > div > ul > li:nth-child(2)").text
        wine_winery = driver.find_element(By.CSS_SELECTOR, "body > div.wrapper > div.container.content-xs > div.row > div.col-md-9.margin-bottom-10 > div > div.row > div.col-md-9 > div > ul > li:nth-child(3)").text
        wine_type = driver.find_element(By.CSS_SELECTOR, "body > div.wrapper > div.container.content-xs > div.row > div.col-md-9.margin-bottom-10 > div > div.row > div.col-md-9 > div > ul > li:nth-child(4)").text
        wine_grape = driver.find_element(By.CSS_SELECTOR, "body > div.wrapper > div.container.content-xs > div.row > div.col-md-9.margin-bottom-10 > div > div.row > div.col-md-9 > div > ul > li:nth-child(5)").text
        wine_image_url = driver.find_element(By.CSS_SELECTOR, "body > div.wrapper > div.container.content-xs > div.row > div.col-md-9.margin-bottom-10 > div > div.row > div.col-md-3 > div > img").get_attribute("src")
        wine_information = driver.find_element(By.CSS_SELECTOR, "#article_1").text
        recommend_count = 0
        #2 이거나 정보가없을때는 담지않으려고 만듬
        if not wine_information or len(wine_information) <= 5:
            print("와인 정보가 없거나 너무 짧습니다.")
            return None

        #ex) r"생산국가/지역 를 그룹화해서 정규식으로선언
        country_pattern = r"생산국가/지역 (.*)"
        country_result = re.search(country_pattern, wine_country)
        # 그룹화된 다음 문자열을 찾고 1번째인덱스저장(그 타이틀저장안하려고함)
        if country_result:
            wine_country = country_result.group(1)

        winery_pattern = r"와이너리 (.*)"
        winery_result = re.search(winery_pattern, wine_winery)
        if winery_result:
            wine_winery = winery_result.group(1)

        type_pattern = r"와인타입 (.*)"
        type_result = re.search(type_pattern, wine_type)
        if type_result:
            wine_type = type_result.group(1)


        grape_pattern = r"포도품종 (.*)"
        grape_result = re.search(grape_pattern, wine_grape)
        if grape_result:
            wine_grape = grape_result.group(1)

        #GUI만들면서 필요한정보들
        if "White Wine" in wine_type:
            wine_type = "WHITE"
        elif "Red Wine" in wine_type:
            wine_type = "RED"
        elif "Rose Wine" in wine_type:
            wine_type = "ROSE"
        elif "Sparkling Wine" in wine_type:
            wine_type = "SPARKLING"
        elif "Ice Wine" in wine_type:
            wine_type = "ICE"

        return {
            "name": wine_name,
            "vintage": wine_vintage,
            "country": wine_country,
            "winery": wine_winery,
            "type": wine_type,
            "grape": wine_grape,
            "image_url": wine_image_url,
            "information": wine_information,
            "recommend_count" : recommend_count
        }

    except Exception as e:
        print("정보를 가져오지 못했습니다. 에러:", e)
        return None
#와인 끝번호
wine_last=30
#페이지 시작번호
page_number = 1
#json/csv저장을위해 담기
wine_list = []

def crawl_wine_data(wine_category, start_page, end_page):
    # wine_type_str 값을 사용하기 위해 와인 카테고리를 문자열로 변환
    if wine_category == "WHITE":
        wine_type_str = "1"
    elif wine_category == "RED":
        wine_type_str = "2"
    elif wine_category == "ROSE":
        wine_type_str = "3"
    elif wine_category == "ICE":
        wine_type_str = "4"
    elif wine_category == "SPARKLING":
        wine_type_str = "5"

    wine_list = []
    page_number = start_page

    # 크롬 드라이버 설정(이거안하면 콘솔창지저분)
    chrome_options = webdriver.ChromeOptions()
    chrome_options.add_argument('--headless')
    chrome_options.add_argument('--no-sandbox')
    chrome_options.add_argument('--disable-dev-shm-usage')

    chrome_options.add_argument("--log-level=3")
    chrome_options.add_argument("--start-maximized")
    chrome_options.add_argument("--disable-infobars")
    chrome_options.add_argument("--disable-extensions")

    # 로깅 레벨(OFF, SEVERE, WARNING, INFO, CONFIG, FINE, FINER, FINEST, ALL)
    prefs = {"loggingPrefs": {"browser": "SEVERE"}}
    chrome_options.add_experimental_option("prefs", prefs)

    # WebDriver 객체를 생성하면서 설정된 옵션을 적용
    driver = webdriver.Chrome(executable_path="chromedriver", options=chrome_options)

    # 페이지 번호가 시작 페이지와 끝 페이지 사이에 있는 동안 크롤링을 반복
    while page_number <= end_page:
        url = f"https://www.wineok.com/?vid=&act=dispWinedbList&mid=winedb&minprice=0&maxprice=100000&style={wine_type_str}&type=&vintage=&import=&country=&ws_keyword=&style_value={wine_type_str}&priceRange=0%3B100000&page={page_number}"

        driver = webdriver.Chrome(service=Service(ChromeDriverManager().install()), options=chrome_options)
        driver.get(url)

        try:
            # 접근
            driver.find_element(By.CSS_SELECTOR, "body > div.wrapper > div.container.content-xs > div.row > div.col-md-9.margin-bottom-10 > div:nth-child(4) > div:nth-child(2)")
            # 다음페이지없으면 종료
        except NoSuchElementException:
            print("마지막 페이지입니다.")
            break

        # 글이 3번부터 있음
        for wine_num in range(1, wine_last + 4):
            try:
                print("현재 페이지 번호:", page_number)
                print(wine_num + 2, "번째")

                element = WebDriverWait(driver, 10).until(
                    EC.element_to_be_clickable((By.CSS_SELECTOR, f"body > div.wrapper > div.container.content-xs > div.row > div.col-md-9.margin-bottom-10 > div:nth-child(4) > div:nth-child({wine_num + 2}) > div.col-md-9 > div.wine-desc > h4 > a"))
                )
                element.click()

                wine_data = extract_wine_information(driver)
                if wine_data:
                    print(f"Wine name: {wine_data['name']}, Wine type: {wine_data['type']}")
                    wine_list.append(wine_data)
                    df = pd.DataFrame(wine_list)
                    df['information'] = df['information'].str.replace('\n', ' ').replace(',', ' ')
                    try:
                        existing_data = pd.read_json('all_wine_data.json', encoding='utf-8')
                    except ValueError:
                        existing_data = pd.DataFrame()
                    all_data = pd.concat([existing_data, df], ignore_index=True)
                    all_data.to_json('all_wine_data.json', orient='records', force_ascii=False)
                    df.to_json(f'{wine_category}_wine_data.json', orient='records', force_ascii=False)

                # 페이지 맨끝번호면 이전 페이지로.
                driver.back()
                if wine_num + 2 == 32:
                    break
            except NoSuchElementException:
                print(f"{wine_num + 2} 번째 와인이 마지막입니다.")
                break

            except TimeoutException:
                print("TimeoutException이 발생했습니다.")
                break

        page_number += 1

    driver.close()


def start_crawling():
    wine_category = wine_category_combobox.get()
    start_page = int(start_page_entry.get())
    end_page = int(end_page_entry.get())
    print(f"Selected wine category: {wine_category}")
    print(f"Start page: {start_page}")
    print(f"End page: {end_page}")
    crawl_wine_data(wine_category, start_page, end_page)


chrome_options = webdriver.ChromeOptions()


#gui는 까먹어서 gpt한테 만들어달라고했음 굳
root = tk.Tk()
root.title("Wine Crawler")

wine_categories = ["WHITE","RED", "ROSE", "ICE", "SPARKLING"]

wine_category_label = ttk.Label(root, text="Wine Category:")
wine_category_label.grid(column=0, row=0, padx=10, pady=10)

wine_category_combobox = ttk.Combobox(root, values=wine_categories)
wine_category_combobox.grid(column=1, row=0, padx=10, pady=10)

start_page_label = ttk.Label(root, text="Start Page:")
start_page_label.grid(column=0, row=1, padx=10, pady=10)

start_page_entry = ttk.Entry(root)
start_page_entry.grid(column=1, row=1, padx=10, pady=10)

end_page_label = ttk.Label(root, text="End Page:")
end_page_label.grid(column=0, row=2, padx=10, pady=10)

end_page_entry = ttk.Entry(root)
end_page_entry.grid(column=1, row=2, padx=10, pady=10)

start_button = ttk.Button(root, text="Start Crawling", command=start_crawling)
start_button.grid(column=1, row=3, padx=10, pady=10)

root.mainloop()






