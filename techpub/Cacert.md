# CAcert för gratis certifikat #

Ett tips i *iText in Action* är gratis cert från https://cacert.org

Deras rotcert finns som standard i Chromium, men inte i Firefox eller Adobe Reader. Man går med genom att registrera sin emailadress. Det finns två slags certifikat, *Client Certificates* och *Server Certificates*. Kanske självklart för den som jobbat mer med dessa saker vad de innebär.


* Client certificate: Utan privat nyckel. Man får ett cert som bara intygar ens mailadress eftersom det är det enda de vet. En mail probe görs. För detta gäller Class 3 rotcert.


* Server certificate: Med privat nyckel. Man får göra ett vanligt *Certificate Signing Request* med hjälp av ex openssl. Först måste man dock registrera en domän. De gör en ytlig koll på att man verkar äga domänen genom att skicka mail till en typisk administratörsadress. Man får välja mellan `root`, `postmaster` och några till. För detta gäller Class 1 rotcert.

Länk till rotcerten finns på startsidan. För att signera något måste man ha ett server cert med denna terminologi.


