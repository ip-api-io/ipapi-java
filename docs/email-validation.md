# Email validation & verification

Check whether an email address is real, deliverable and safe to accept — before it
ever enters your database. The SDK exposes three levels: a fast syntax/MX/disposable
check, full SMTP verification, and a batch endpoint for cleaning whole lists.

Powers [email validation](https://ip-api.io/email-validation),
[advanced email validation](https://ip-api.io/advanced-email-validation),
[email verification](https://ip-api.io/email-verification-api),
[disposable email detection](https://ip-api.io/disposable-email-checker) and
[email list cleaning](https://ip-api.io/email-list-cleaning).

## `emailInfo(email)` — fast syntax, MX & disposable check

A lightweight check (no SMTP probe): validates syntax, confirms the domain has MX
records, and flags disposable/throwaway providers. Use it inline on sign-up forms.

```java
import io.ipapi.IpApiClient;
import io.ipapi.model.EmailInfo;

IpApiClient client = IpApiClient.builder().apiKey("YOUR_API_KEY").build();

EmailInfo info = client.emailInfo("user@example.com");

System.out.println(info.syntax().isValid()); // true
System.out.println(info.isDisposable());     // false
System.out.println(info.hasMxRecords());     // true
if (!info.mxRecords().isEmpty()) {
    System.out.println(info.mxRecords().get(0).hostname());
}
```

### Response (`EmailInfo`)

| Accessor | Type | Description |
|---|---|---|
| `email()` | `String` | The address checked |
| `isDisposable()` | `boolean` | Throwaway / temporary provider |
| `hasMxRecords()` | `boolean` | Domain can receive mail |
| `mxRecords()` | `List<MxRecord>` | Each: `priority()`, `hostname()`, `ttl()` |
| `syntax()` | `EmailSyntax` | `isValid()`, `domain()`, `username()`, `errorReasons()` |

## `validateEmail(email)` — full SMTP deliverability

Advanced verification that connects to the mail server to confirm the mailbox is
deliverable, and adds role-account, free-provider, catch-all and Gravatar signals.
Use it before sending important mail or accepting a paying customer.

```java
import io.ipapi.model.AdvancedEmailValidation;

AdvancedEmailValidation result = client.validateEmail("user@example.com");

System.out.println(result.reachable());      // "yes" | "no" | "unknown"
if (result.smtp() != null) {
    System.out.println(result.smtp().deliverable());
    System.out.println(result.smtp().catchAll());
}
System.out.println(result.disposable());     // false
System.out.println(result.roleAccount());    // false  (e.g. info@, support@)
System.out.println(result.free());           // false  (e.g. gmail.com)
System.out.println(result.suggestion());     // typo fix, e.g. "user@gmail.com"
```

### Response (`AdvancedEmailValidation`)

| Accessor | Type | Description |
|---|---|---|
| `email()` | `String` | The address checked |
| `reachable()` | `String` | `"yes"`, `"no"` or `"unknown"` |
| `syntax()` | `AdvancedSyntax` | `username()`, `domain()`, `valid()` |
| `smtp()` | `AdvancedSmtp` | `hostExists()`, `deliverable()`, `fullInbox()`, `catchAll()`, `disabled()` |
| `gravatar()` | `AdvancedGravatar` | `hasGravatar()`, `gravatarUrl()` |
| `suggestion()` | `String` | Suggested correction for a likely typo |
| `disposable()` | `boolean` | Throwaway provider |
| `roleAccount()` | `boolean` | Role address (info@, sales@, …) |
| `free()` | `boolean` | Free webmail provider |
| `hasMxRecords()` | `boolean` | Domain can receive mail |

## `validateEmailBatch(emails)` — clean a list (≤100)

Advanced-validate up to 100 addresses in one request — the building block for
[email list cleaning](https://ip-api.io/email-list-cleaning). Throws
`IllegalArgumentException` if the list is empty or longer than 100.

```java
import io.ipapi.model.BatchEmailValidationResponse;
import java.util.List;

BatchEmailValidationResponse batch = client.validateEmailBatch(
        List.of("user@example.com", "fake@mailinator.com"));

System.out.println(batch.totalProcessed());        // 2
System.out.println(batch.successfulValidations()); // 2

batch.results().forEach((email, result) ->
        System.out.println(email + " " + result.reachable()));
```

### Response (`BatchEmailValidationResponse`)

| Accessor | Type | Description |
|---|---|---|
| `results()` | `Map<String, AdvancedEmailValidation>` | Map of email → result |
| `totalProcessed()` | `int` | Emails received |
| `successfulValidations()` | `int` | Emails validated |
| `failedValidations()` | `int` | Emails that errored |

## See also

- [Fraud detection & risk scoring](fraud-risk-scoring.md) — `emailRiskScore` for a 0–100 score
- [ASN & DNS lookups](asn-and-dns.md) — `mxRecords` to inspect a domain's mail servers
- Product pages: [Email validation](https://ip-api.io/email-validation) · [Advanced validation](https://ip-api.io/advanced-email-validation) · [Email verification API](https://ip-api.io/email-verification-api) · [Disposable email checker](https://ip-api.io/disposable-email-checker) · [Email list cleaning](https://ip-api.io/email-list-cleaning)
