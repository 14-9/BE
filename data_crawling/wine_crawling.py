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


# value=2&priceRange=0%3B100000 10만원이하
# value=1&priceRange=0%3B100000 10만원이하
# value=1 화이트와인 
# value=2  레드와인 


chrome_options = webdriver.ChromeOptions()


#긁어올 정보
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
        
        #ex_ r"생산국가/지역 를 그룹화해서 
        country_pattern = r"생산국가/지역 (.*)"
        country_result = re.search(country_pattern, wine_country)
        # 그룹화된 다음 문자열을 찾는다
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

while True:
    
    url = f"https://www.wineok.com/?vid=&act=dispWinedbList&mid=winedb&minprice=0&maxprice=100000&style=2&type=&vintage=&import=&country=&ws_keyword=&style_value=2&priceRange=0%3B100000&page={page_number}"
    #크롬드라이버연결
    driver = webdriver.Chrome(service=Service(ChromeDriverManager().install()), options=chrome_options)
    driver.get(url)
    try:
        #접근
        driver.find_element(By.CSS_SELECTOR, "body > div.wrapper > div.container.content-xs > div.row > div.col-md-9.margin-bottom-10 > div:nth-child(4) > div:nth-child(2)")
        #다음페이지없으면 종료
    except NoSuchElementException:
        print("마지막 페이지입니다.")
        break
    #글이 3번부터있음
    for wine_num in range(1, wine_last + 4):
        try:
            print("현재 페이지 번호:", page_number)
            print(wine_num+2,"번째")
            #셀레니움 객체driver와 시간을설정(여기선10초)을하고
            #로드가완료되면 다음단계실행
            element = WebDriverWait(driver, 10).until(
            #CSS 선택자를 사용하여 접근한다
            EC.element_to_be_clickable((By.CSS_SELECTOR, f"body > div.wrapper > div.container.content-xs > div.row > div.col-md-9.margin-bottom-10 > div:nth-child(4) > div:nth-child({wine_num+2}) > div.col-md-9 > div.wine-desc > h4 > a"))
            )
            element.click()
           
            wine_data = extract_wine_information(driver)
            if wine_data:
                print(wine_data)
                #wine_data를 만들어놓은 list에담고
                wine_list.append(wine_data)
                #데이터 프레임으로 전환
                df = pd.DataFrame(wine_list)
                #인포메이션중에 \n과 , 제거(했는데 몇개나옴)
                df['information'] = df['information'].str.replace('\n', ' ').replace(',', ' ')
                #이건 csv파일로 저장하는것
                # df.to_csv('white_wine_data.csv', index=False, encoding='utf-8-sig')
                #이건 제이슨파일로 저장하는것
                df.to_json('red_wine_final_hope.json', orient='records', force_ascii=False)

            # 페이지 맨끝번호면 이전 페이지로.
            driver.back()
            if wine_num+2 ==32 :
                break
            
        except NoSuchElementException:
            print(f"{wine_num+2} 번째 와인이 마지막입니다.")
            break

        except TimeoutException:
            print("TimeoutException이 발생했습니다.")
            break
    page_number += 1
    
driver.close()


