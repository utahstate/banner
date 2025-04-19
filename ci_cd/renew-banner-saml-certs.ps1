$MSGRAPH_CLIENT_ID = $env:MSGRAPH_CLIENT_ID
$MSGRAPH_CLIENT_SECRET = $env:MSGRAPH_CLIENT_SECRET
$MSGRAPH_TENANT = $env:MSGRAPH_TENANT
#$ZPPRD_IDS=@('5e0d8695-0b27-4143-bc79-5d854fed4762','6bfafcce-8b9e-482a-9905-6eb7aa062679','29dfa462-1774-4b02-8691-f936b2442ca1','681d8f38-ef8e-4a8a-94b7-7710c64e6b38','84e95550-9654-41a7-869b-6fdf40aa8eba','97576e66-010e-4bee-9ee0-319062459b55','b654c093-280b-4104-be41-e452dcf44079','dcc88ba2-e50a-4c59-a0cb-7fb7e6e6a6c3','e8ff9797-9dc1-4977-aaf6-41c0172630e5')
$APP_IDS = $env:APP_IDS
$DB=$env:DB
$KEYSTORE_PASSWORD=$env:KEYSTORE_PASSWORD
$KEYSTORE_FILE=$DB + '_keystore.jks'
$Body =  @{
    Grant_Type    = "client_credentials"
    Scope         = "https://graph.microsoft.com/.default"
    Client_Id     = $MSGRAPH_CLIENT_ID
    Client_Secret = $MSGRAPH_CLIENT_SECRET
}
#Write-Host ($body)
$Connection = Invoke-RestMethod -Uri https://login.microsoftonline.com/$MSGRAPH_TENANT/oauth2/v2.0/token -Method POST -Body $body
$Token = $Connection.access_token
Connect-MgGraph -AccessToken ($Token |ConvertTo-SecureString -AsPlainText -Force)

cd /opt/mount

foreach ($Id in $APP_IDS) {
    $SP = Get-MgServicePrincipal -Filter "Id eq '$Id'"
    Write-Host ('Updating SAML cert for', $SP.DisplayName) 
    $NewCert = Add-MgServicePrincipalTokenSigningCertificate -ServicePrincipalId $Id
    $certKey = $NewCert.Key
    $certbase64 = [Convert]::ToBase64String($certKey)
    $certfile = $SP.DisplayName + '.cer'
    "-----BEGIN CERTIFICATE-----" | Out-File -FilePath $certfile -Encoding Ascii
    $certbase64 | Out-File -FilePath $certfile -Encoding Ascii -Append
    "-----END CERTIFICATE-----" | Out-File -FilePath $certfile -Encoding Ascii -Append
    $string_array = $SP.DisplayName -split "-"
    $alias = $DB + '-' + $string_array[2].ToLower() + '-idp'
    keytool -delete -keystore $KEYSTORE_FILE -alias $alias -storepass $KEYSTORE_PASSWORD -noprompt
    keytool -importcert -file $certfile -keystore $KEYSTORE_FILE -alias $alias -storepass $KEYSTORE_PASSWORD -noprompt
    $params = @{
        preferredTokenSigningKeyThumbprint = $NewCert.Thumbprint
    }
    Update-MgServicePrincipal -ServicePrincipalId $Id -BodyParameter $params
    $url = 'https://login.microsoftonline.com/ac352f9b-eb63-4ca2-9cf9-f4c40047ceff/federationmetadata/2007-06/federationmetadata.xml?appid=' + $SP.AppId
    $metadata_file_path = $DB + '-' + $string_array[2].ToLower() + '-idp.xml'
    Invoke-WebRequest -Uri $url -OutFile $metadata_file_path
}
kubectl rollout restart deployment applicationnavigator -n $DB
kubectl rollout restart deployment bannerextensibility -n $DB
kubectl rollout restart deployment studentselfservice -n $DB
kubectl rollout restart deployment financeselfservice -n $DB
kubectl rollout restart deployment communicationmanagement -n $DB
kubectl rollout restart deployment studentregistrationssb -n $DB
kubectl rollout restart deployment bannergeneralssb -n $DB
kubectl rollout restart deployment employeeselfservice -n $DB
kubectl rollout restart deployment facultyselfservice -n $DB

    
## TODO ##
## Generate new cert
## Get new cert
## Save new cert to file
## Import new cert to Java keystore
## Mark new cert as active
## Get new metadata
## Save new metadata file to disk
## Restart app
