from selenium import webdriver
import time
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.common.keys import Keys
import os

chrome_options = Options()
chrome_options.add_argument("--no-sandbox")
chrome_options.add_argument("--headless")
chrome_options.add_argument("--disable-gpu")
chrome_options.add_argument("--disable-dev-shm-usage")
chrome_options.add_argument("--start-maximized")
chrome_options.add_argument("--allow-running-insecure-content")
chrome_options.add_argument("--ignore-certificate-errors")

#driver = webdriver.Chrome(chrome_options=chrome_options)
driver = webdriver.Chrome(options=chrome_options)

driver.get('http://172.17.0.3:8081/artifactory/webapp/#/login')
time.sleep(5)

userName = driver.find_element_by_name('user')
userName.send_keys('admin')
password = driver.find_element_by_name('password')
password.send_keys('password123')

loginForm = driver.find_element_by_name('Login.loginForm')
loginForm.submit()
time.sleep(2)

#driver.get('http://localhost:8081/artifactory/webapp/#/admin/repositories/local')

#time.sleep(2)
driver.get('http://172.17.0.3:8081/artifactory/webapp/#/admin/repository/local/new')
time.sleep(2)

filterName = driver.find_element_by_id('package-type-filter')
filterName.send_keys('generic')
filterName.send_keys(Keys.ENTER)

repoName = driver.find_element_by_id('repoKey-new').send_keys(os.getenv('app_name'))
time.sleep(2)
saveButton = driver.find_element_by_id('repository-save-button')
saveButton.submit()

time.sleep(5)

driver.quit()
