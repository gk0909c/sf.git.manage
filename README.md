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
+ create setting.yml in classpath, like this.
  
  ```yaml
  # put repository info you can access.
  repository: !!gk0909c.sf.git.manage.git.RepositoryInfo
    user: user
    pw: password
    uri: http://example.com:8080/your/repository.git
    repoName: yourRepsitory
    localBase: C:/localtion/ 
    branchName: yourBranch
  # put sfdc info you can access.
  sfdc: !!gk0909c.sf.git.manage.sfdc.SfdcInfo
    user: user
    pw: password
    securityToken: securityToken
    partnerUri: https://test.salesforce.com/services/Soap/u/35.0
  # zip and deploy metadata location.
  zip: !!gk0909c.sf.git.manage.zip.ZipInfo
    zipPath: C:/location/metadata.zip
    baseDir: C:/localtion/repository/sfdc-src-path

  ```
  
## Unit test ##
+ To Execute gk0909c.sf.git.manage.git.TestGitOperator,  
  Make yaml file in classpath.  (only repository info. no need hierarchy.)
  + testGetReporsitory01.yml, with no user, no pw.
  + testGetReporsitory02.yml, with your user, your pw.
+ To Execute gk0909c.sf.git.manage.sfdc.TestSfdcDeployer,  
  Make yaml file(testDeployMetadata.yml) in classpath.  (only sfdc info. no need hierarchy.)
  
## behind proxy ##
use jvm args.  
+ https.proxyHost
+ https.proxyPort
+ https.proxyUser
+ https.proxyPassword