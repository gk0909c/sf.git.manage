# git管理しているSFDCリソースをデプロイしてテスト #
開発中

## git操作 ##
+ 初回はリポジトリクローン
+ 特定ブランに切替
+ ブランチを最新化

## SFDC操作 ##
+ 最新化したリソースをデプロイ
+ Apexテスト実行

## Run ##
+ create reporsitoryInfo.yml in classpath, like this.
  
  ```yaml
  # put repository info you can access.
  user: your user      # if you needs credential
  pw: your password    # if you needs credential
  uri: https://github.com/your/repository.git
  repoName: your.repository
  localBase: C:/git-temp/ 
  branchName: master
  ```
  
## Unit test ##
+ To Execute gk0909c.sf.git.manage.git.TestGitOperator,  
  Make yaml file in classpath.  
  + testGetReporsitory01.yml is no user, no pw.
  + testGetReporsitory02.yml is your user, your pw.
  
