# git管理しているSFDCリソースをデプロイしてテスト #
開発中

## git操作 ##
+ 初回はリポジトリクローン
+ 特定ブランに切替
+ ブランチを最新化

## SFDC操作 ##
+ 最新化したリソースをデプロイ
+ Apexテスト実行

## Unit test ##
+ To Execute gk0909c.sf.git.manage.git.TestGitOperator,  
  Make yaml file in src/test/resources/TestGitOperator.  
  Like this.  
  
  ```yaml
  # put repository info you can access.
  # testGetReporsitory01.yml is no user, no pw.
  # testGetReporsitory01.yml is your user, your pw.
  user: 
  pw: 
  uri: https://github.com/satk0909/sf.git.manage.git
  repoName: sf.git.manage
  localBase: C:/git-temp/ 
  ```
  
