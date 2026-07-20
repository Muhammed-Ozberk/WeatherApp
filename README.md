# Hava Durumu

Türkiye'deki şehirler için güncel, yarın ve haftalık hava durumu bilgilerini gösteren Android uygulaması. Uygulama Kotlin ile geliştirilmiştir; CollectAPI hava durumu servisini Retrofit üzerinden kullanır.

## Özellikler

- Şehir arama ve seçme
- Bugünün ve yarının hava durumu
- Haftalık hava durumu listesi
- Sıcaklık, nem, açıklama ve hava durumu görselleri

## Gereksinimler

- Android Studio
- JDK 17
- Android SDK 33
- CollectAPI hava durumu API anahtarı

## Kurulum

1. Projeyi klonlayın.
2. `local.properties.example` dosyasını `local.properties` adıyla kopyalayın.
3. `sdk.dir` değerini Android SDK yolunuzla değiştirin.
4. `WEATHER_API_KEY` değerine CollectAPI'nin beklediği tam `Authorization` başlık değerini yazın:

```properties
WEATHER_API_KEY=apikey your-api-key
```

Alternatif olarak anahtarı ortam değişkeniyle verebilirsiniz. Ortam değişkeni, `local.properties` değerinden önceliklidir:

```bash
export WEATHER_API_KEY="apikey your-api-key"
./gradlew assembleDebug
```

`local.properties` Git tarafından izlenmez. API anahtarını repoya eklemeyin. Anahtar tanımlanmadığında proje derlenebilir ancak API istekleri yetkilendirme hatası alır.

## Testler

Birim testlerini çalıştırmak için:

```bash
./gradlew testDebugUnitTest
```

Testler repository'nin başarılı, başarısız ve ağ hatası yanıtlarını; ayrıca `SharedViewModel` şehir seçimi durumunu kapsar.

## Teknolojiler

- Kotlin ve Android View Binding
- AndroidX ViewModel ve LiveData
- Retrofit ve Gson
- Picasso
- JUnit, Mockito ve AndroidX Architecture Components Test

## Lisans

Bu proje [MIT Lisansı](LICENSE) altında lisanslanmıştır.
