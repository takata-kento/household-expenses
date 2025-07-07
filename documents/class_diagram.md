# 家計簿アプリ クラス図

```mermaid
classDiagram
    %% エンティティクラス
    class User {
        -Username username
        -UserGroupId userGroupId
        +getUsername() Username
        +getUserGroupId() UserGroupId
    }

    class UserGroup {
        -UserGroupId id
        -GroupName groupName
        -Day monthStartDay
        -Username createdByUsername
        +getId() UserGroupId
        +getGroupName() GroupName
        +getMonthStartDay() Day
        +getCreatedByUsername() Username
    }

    class GroupInvitation {
        -UserGroupId userGroupId
        -Username invitedUsername
        -Username invitedByUsername
        -InvitationStatus status
        -LocalDateTime invitedAt
        -LocalDateTime respondedAt
        +getUserGroupId() UserGroupId
        +getInvitedUsername() Username
        +getInvitedByUsername() Username
        +getStatus() InvitationStatus
    }

    class FinancialAccount {
        -FinancialAccountId id
        -Username username
        -AccountName accountName
        -Money balance
        -Boolean isMainAccount
        +getId() FinancialAccountId
        +getUsername() Username
        +getAccountName() AccountName
        +getBalance() Money
        +getIsMainAccount() Boolean
    }

    class BalanceEditHistory {
        -BalanceEditHistoryId id
        -FinancialAccountId financialAccountId
        -Money oldBalance
        -Money newBalance
        -Description editReason
        -LocalDateTime createdAt
        +getId() BalanceEditHistoryId
        +getFinancialAccountId() FinancialAccountId
        +getOldBalance() Money
        +getNewBalance() Money
        +getEditReason() Description
    }

    class MonthlyBudget {
        -UserGroupId userGroupId
        -Year year
        -Month month
        -Money budgetAmount
        -Username setByUsername
        +getUserGroupId() UserGroupId
        +getYear() Year
        +getMonth() Month
        +getBudgetAmount() Money
        +getSetByUsername() Username
    }

    class LivingExpenseCategory {
        -LivingExpenseCategoryId id
        -UserGroupId userGroupId
        -CategoryName categoryName
        -Description description
        -Boolean isDefault
        +getId() LivingExpenseCategoryId
        +getUserGroupId() UserGroupId
        +getCategoryName() CategoryName
        +getDescription() Description
        +getIsDefault() Boolean
    }

    class DailyTransaction {
        -Username username
        -LocalDate transactionDate
        -Money income
        -Money totalExpense
        -Money personalExpense
        -FinancialAccountId financialAccountId
        +getUsername() Username
        +getTransactionDate() LocalDate
        +getIncome() Money
        +getTotalExpense() Money
        +getPersonalExpense() Money
        +getFinancialAccountId() FinancialAccountId
    }

    class DailyLivingExpense {
        -DailyLivingExpenseId id
        -Username username
        -LocalDate transactionDate
        -LivingExpenseCategoryId livingExpenseCategoryId
        -Money amount
        -Description memo
        +getId() DailyLivingExpenseId
        +getUsername() Username
        +getTransactionDate() LocalDate
        +getLivingExpenseCategoryId() LivingExpenseCategoryId
        +getAmount() Money
        +getMemo() Description
    }

    class DailyPersonalExpense {
        -Username username
        -LocalDate transactionDate
        -SequenceNumber sequenceNo
        -Money amount
        -Description description
        +getUsername() Username
        +getTransactionDate() LocalDate
        +getSequenceNo() SequenceNumber
        +getAmount() Money
        +getDescription() Description
    }

    class DailyBudgetBalance {
        -UserGroupId userGroupId
        -LocalDate transactionDate
        -Money totalLivingExpense
        -Money budgetBalance
        +getUserGroupId() UserGroupId
        +getTransactionDate() LocalDate
        +getTotalLivingExpense() Money
        +getBudgetBalance() Money
    }

    class FixedExpenseCategory {
        -FixedExpenseCategoryId id
        -UserGroupId userGroupId
        -CategoryName categoryName
        -Description description
        -Money defaultAmount
        +getId() FixedExpenseCategoryId
        +getUserGroupId() UserGroupId
        +getCategoryName() CategoryName
        +getDescription() Description
        +getDefaultAmount() Money
    }

    class FixedExpenseHistory {
        -FixedExpenseHistoryId id
        -FixedExpenseCategoryId fixedExpenseCategoryId
        -Year year
        -Month month
        -Money amount
        -LocalDate effectiveDate
        -Description memo
        +getId() FixedExpenseHistoryId
        +getFixedExpenseCategoryId() FixedExpenseCategoryId
        +getYear() Year
        +getMonth() Month
        +getAmount() Money
        +getEffectiveDate() LocalDate
        +getMemo() Description
    }

    class MonthlySaving {
        -Username username
        -Year year
        -Month month
        -Money savingAmount
        -FinancialAccountId financialAccountId
        -Description memo
        +getUsername() Username
        +getYear() Year
        +getMonth() Month
        +getSavingAmount() Money
        +getFinancialAccountId() FinancialAccountId
        +getMemo() Description
    }

    %% リクエストクラス
    class UserRegistrationRequest {
        -Username username
        -String password
        +getUsername() Username
        +getPassword() String
    }

    class UserLoginRequest {
        -Username username
        -String password
        +getUsername() Username
        +getPassword() String
    }

    class DailyTransactionRequest {
        -LocalDate transactionDate
        -Money income
        -List~DailyLivingExpenseRequest~ livingExpenses
        -List~DailyPersonalExpenseRequest~ personalExpenses
        +getTransactionDate() LocalDate
        +getIncome() Money
        +getLivingExpenses() List~DailyLivingExpenseRequest~
        +getPersonalExpenses() List~DailyPersonalExpenseRequest~
    }

    class DailyLivingExpenseRequest {
        -Long categoryId
        -Money amount
        -Description memo
        +getCategoryId() Long
        +getAmount() Money
        +getMemo() Description
    }

    class DailyPersonalExpenseRequest {
        -Money amount
        -Description description
        +getAmount() Money
        +getDescription() Description
    }

    class MonthlyBudgetRequest {
        -Year year
        -Month month
        -Money budgetAmount
        +getYear() Year
        +getMonth() Month
        +getBudgetAmount() Money
    }

    class GroupInvitationRequest {
        -Username invitedUsername
        +getInvitedUsername() Username
    }

    %% リクエスト間の関係性
    DailyTransactionRequest o-- DailyLivingExpenseRequest
    DailyTransactionRequest o-- DailyPersonalExpenseRequest

    %% 値オブジェクトクラス
    class Money {
        -BigDecimal amount
        +getAmount() BigDecimal
        +add(Money) Money
        +subtract(Money) Money
        +isPositive() boolean
        +isZero() boolean
    }

    class Year {
        -Integer value
        +getValue() Integer
        +isValid() boolean
        +next() Year
        +previous() Year
    }

    class Month {
        -Integer value
        +getValue() Integer
        +isValid() boolean
        +next() Month
        +previous() Month
    }

    class Username {
        -String value
        +getValue() String
        +isValid() boolean
        +length() int
    }


    class CategoryName {
        -String value
        +getValue() String
        +isValid() boolean
        +length() int
    }

    class Description {
        -String value
        +getValue() String
        +isValid() boolean
        +length() int
        +isEmpty() boolean
    }

    class AccountName {
        -String value
        +getValue() String
        +isValid() boolean
        +length() int
    }

    class UserGroupId {
        -Long value
        +getValue() Long
        +isValid() boolean
    }

    class FinancialAccountId {
        -Long value
        +getValue() Long
        +isValid() boolean
    }

    class Day {
        -Integer value
        +getValue() Integer
        +isValid() boolean
        +isValidForMonth(Month) boolean
    }

    class GroupName {
        -String value
        +getValue() String
        +isValid() boolean
        +length() int
    }

    class SequenceNumber {
        -Integer value
        +getValue() Integer
        +isValid() boolean
        +isPositive() boolean
    }

    class LivingExpenseCategoryId {
        -Long value
        +getValue() Long
        +isValid() boolean
    }

    class FixedExpenseCategoryId {
        -Long value
        +getValue() Long
        +isValid() boolean
    }

    class BalanceEditHistoryId {
        -Long value
        +getValue() Long
        +isValid() boolean
    }

    class DailyLivingExpenseId {
        -Long value
        +getValue() Long
        +isValid() boolean
    }

    class FixedExpenseHistoryId {
        -Long value
        +getValue() Long
        +isValid() boolean
    }

    %% Enumクラス
    class InvitationStatus {
        <<enumeration>>
        PENDING
        ACCEPTED
        REJECTED
        +isPending() boolean
        +isAccepted() boolean
        +isRejected() boolean
    }

    %% サービスクラス
    class UserService {
        -UserRepository userRepository
        -GroupInvitationRepository groupInvitationRepository
        -PasswordEncoder passwordEncoder
        +registerUser(UserRegistrationRequest) User
        +authenticateUser(UserLoginRequest) User
        +getCurrentUser() User
        +findByUsername(String) Optional~User~
        +acceptGroupInvitation(Long) void
        +rejectGroupInvitation(Long) void
    }

    class UserGroupService {
        -UserGroupRepository userGroupRepository
        -GroupInvitationRepository groupInvitationRepository
        -UserRepository userRepository
        +createGroup(String) UserGroup
        +inviteUser(GroupInvitationRequest) GroupInvitation
        +leaveGroup(Long) void
        +getGroupMembers(Long) List~User~
    }

    class TransactionService {
        -DailyTransactionRepository dailyTransactionRepository
        -DailyLivingExpenseRepository dailyLivingExpenseRepository
        -DailyPersonalExpenseRepository dailyPersonalExpenseRepository
        -BudgetService budgetService
        +recordDailyTransaction(DailyTransactionRequest) DailyTransaction
        +calculateTotalExpense(List~DailyLivingExpenseRequest~, List~DailyPersonalExpenseRequest~, Integer) BigDecimal
        +getDailyTransaction(Long, LocalDate) DailyTransaction
        +updateDailyTransaction(Long, LocalDate, DailyTransactionRequest) DailyTransaction
        +deleteDailyTransaction(Long, LocalDate) void
    }

    class BudgetService {
        -MonthlyBudgetRepository monthlyBudgetRepository
        -DailyBudgetBalanceRepository dailyBudgetBalanceRepository
        +setMonthlyBudget(MonthlyBudgetRequest) MonthlyBudget
        +getMonthlyBudget(Long, Integer, Integer) MonthlyBudget
        +calculateBudgetBalance(Long, LocalDate, BigDecimal) BigDecimal
        +updateDailyBudgetBalance(Long, LocalDate, BigDecimal) DailyBudgetBalance
    }

    class ExpenseService {
        -LivingExpenseCategoryRepository livingExpenseCategoryRepository
        -FixedExpenseCategoryRepository fixedExpenseCategoryRepository
        -FixedExpenseHistoryRepository fixedExpenseHistoryRepository
        +createLivingExpenseCategory(Long, String, String) LivingExpenseCategory
        +updateLivingExpenseCategory(Long, String, String) LivingExpenseCategory
        +deleteLivingExpenseCategory(Long) void
        +createFixedExpenseCategory(Long, String, String, BigDecimal) FixedExpenseCategory
        +setFixedExpenseAmount(Long, Integer, Integer, BigDecimal) FixedExpenseHistory
        +getFixedExpenses(Long, Integer, Integer) List~FixedExpenseHistory~
    }

    class AccountService {
        -FinancialAccountRepository financialAccountRepository
        -BalanceEditHistoryRepository balanceEditHistoryRepository
        +createAccount(Long, String, BigDecimal, Boolean) FinancialAccount
        +updateBalance(Long, BigDecimal) FinancialAccount
        +editBalance(Long, BigDecimal, String) FinancialAccount
        +calculateNewBalance(Long, BigDecimal, BigDecimal, BigDecimal, BigDecimal) BigDecimal
        +getUserAccounts(Long) List~FinancialAccount~
    }

    class SavingService {
        -MonthlySavingRepository monthlySavingRepository
        +recordMonthlySaving(Long, Integer, Integer, BigDecimal, Long, String) MonthlySaving
        +getMonthlySaving(Long, Integer, Integer) MonthlySaving
        +updateMonthlySaving(Long, Integer, Integer, BigDecimal, String) MonthlySaving
        +deleteMonthlySaving(Long, Integer, Integer) void
    }

    %% コントローラークラス
    class UserController {
        -UserService userService
        +register(UserRegistrationRequest) ResponseEntity~User~
        +login(UserLoginRequest) ResponseEntity~String~
        +getCurrentUser() ResponseEntity~User~
        +logout() ResponseEntity~Void~
        +acceptGroupInvitation(Long) ResponseEntity~Void~
        +rejectGroupInvitation(Long) ResponseEntity~Void~
    }

    class UserGroupController {
        -UserGroupService userGroupService
        +createGroup(String) ResponseEntity~UserGroup~
        +inviteUser(GroupInvitationRequest) ResponseEntity~GroupInvitation~
        +leaveGroup() ResponseEntity~Void~
        +getGroupMembers() ResponseEntity~List~User~~
    }

    class TransactionController {
        -TransactionService transactionService
        +recordTransaction(DailyTransactionRequest) ResponseEntity~DailyTransaction~
        +getTransaction(LocalDate) ResponseEntity~DailyTransaction~
        +updateTransaction(LocalDate, DailyTransactionRequest) ResponseEntity~DailyTransaction~
        +deleteTransaction(LocalDate) ResponseEntity~Void~
    }

    class BudgetController {
        -BudgetService budgetService
        +setMonthlyBudget(MonthlyBudgetRequest) ResponseEntity~MonthlyBudget~
        +getMonthlyBudget(Integer, Integer) ResponseEntity~MonthlyBudget~
        +getBudgetBalance(LocalDate) ResponseEntity~DailyBudgetBalance~
    }

    class ExpenseController {
        -ExpenseService expenseService
        +createLivingExpenseCategory(String, String) ResponseEntity~LivingExpenseCategory~
        +updateLivingExpenseCategory(Long, String, String) ResponseEntity~LivingExpenseCategory~
        +deleteLivingExpenseCategory(Long) ResponseEntity~Void~
        +createFixedExpenseCategory(String, String, BigDecimal) ResponseEntity~FixedExpenseCategory~
        +setFixedExpenseAmount(Long, Integer, Integer, BigDecimal) ResponseEntity~FixedExpenseHistory~
        +getFixedExpenses(Integer, Integer) ResponseEntity~List~FixedExpenseHistory~~
    }

    class AccountController {
        -AccountService accountService
        +createAccount(String, BigDecimal, Boolean) ResponseEntity~FinancialAccount~
        +updateBalance(Long, BigDecimal) ResponseEntity~FinancialAccount~
        +editBalance(Long, BigDecimal, String) ResponseEntity~FinancialAccount~
        +getUserAccounts() ResponseEntity~List~FinancialAccount~~
    }

    class SavingController {
        -SavingService savingService
        +recordMonthlySaving(Integer, Integer, BigDecimal, Long, String) ResponseEntity~MonthlySaving~
        +getMonthlySaving(Integer, Integer) ResponseEntity~MonthlySaving~
        +updateMonthlySaving(Integer, Integer, BigDecimal, String) ResponseEntity~MonthlySaving~
        +deleteMonthlySaving(Integer, Integer) ResponseEntity~Void~
    }

    %% リポジトリインターフェース
    class UserRepository {
        <<interface>>
        +findByUsername(String) Optional~User~
        +existsByUsername(String) boolean
    }

    class UserGroupRepository {
        <<interface>>
        +findByCreatedByUsername(Long) Optional~UserGroup~
    }

    class GroupInvitationRepository {
        <<interface>>
        +findByInvitedUsernameAndStatus(Long, String) List~GroupInvitation~
        +findByUserGroupIdAndInvitedUsername(Long, Long) Optional~GroupInvitation~
    }

    class FinancialAccountRepository {
        <<interface>>
        +findByUsername(Long) List~FinancialAccount~
        +findByUsernameAndIsMainAccount(Long, Boolean) Optional~FinancialAccount~
    }

    class BalanceEditHistoryRepository {
        <<interface>>
        +findByFinancialAccountIdOrderByIdDesc(Long) List~BalanceEditHistory~
    }

    class MonthlyBudgetRepository {
        <<interface>>
        +findByUserGroupIdAndYearAndMonth(Long, Integer, Integer) Optional~MonthlyBudget~
    }

    class LivingExpenseCategoryRepository {
        <<interface>>
        +findByUserGroupId(Long) List~LivingExpenseCategory~
        +findByUserGroupIdAndIsDefault(Long, Boolean) List~LivingExpenseCategory~
    }

    class DailyTransactionRepository {
        <<interface>>
        +findByUsernameAndTransactionDate(Long, LocalDate) Optional~DailyTransaction~
        +findByUsernameAndTransactionDateBetween(Long, LocalDate, LocalDate) List~DailyTransaction~
    }

    class DailyLivingExpenseRepository {
        <<interface>>
        +findByUsernameAndTransactionDate(Long, LocalDate) List~DailyLivingExpense~
        +findByUsernameAndTransactionDateBetween(Long, LocalDate, LocalDate) List~DailyLivingExpense~
    }

    class DailyPersonalExpenseRepository {
        <<interface>>
        +findByUsernameAndTransactionDate(Long, LocalDate) List~DailyPersonalExpense~
        +findByUsernameAndTransactionDateBetween(Long, LocalDate, LocalDate) List~DailyPersonalExpense~
    }

    class DailyBudgetBalanceRepository {
        <<interface>>
        +findByUserGroupIdAndTransactionDate(Long, LocalDate) Optional~DailyBudgetBalance~
        +findByUserGroupIdAndTransactionDateBetween(Long, LocalDate, LocalDate) List~DailyBudgetBalance~
    }

    class FixedExpenseCategoryRepository {
        <<interface>>
        +findByUserGroupId(Long) List~FixedExpenseCategory~
    }

    class FixedExpenseHistoryRepository {
        <<interface>>
        +findByFixedExpenseCategoryIdAndYearAndMonth(Long, Integer, Integer) Optional~FixedExpenseHistory~
        +findByFixedExpenseCategoryIdInAndYear(List~Long~, Integer) List~FixedExpenseHistory~
    }

    class MonthlySavingRepository {
        <<interface>>
        +findByUsernameAndYearAndMonth(Long, Integer, Integer) Optional~MonthlySaving~
        +findByUsernameAndYear(Long, Integer) List~MonthlySaving~
    }

    %% 値オブジェクトとの関係性
    User o-- Username
    User o-- UserGroupId
    UserGroup o-- UserGroupId
    UserGroup o-- GroupName
    UserGroup o-- Day
    GroupInvitation o-- UserGroupId
    GroupInvitation o-- InvitationStatus
    FinancialAccount o-- FinancialAccountId
    FinancialAccount o-- AccountName
    FinancialAccount o-- Money
    BalanceEditHistory o-- BalanceEditHistoryId
    BalanceEditHistory o-- FinancialAccountId
    BalanceEditHistory o-- Money
    BalanceEditHistory o-- Description
    DailyTransaction o-- FinancialAccountId
    MonthlySaving o-- FinancialAccountId
    MonthlyBudget o-- UserGroupId
    MonthlyBudget o-- Year
    MonthlyBudget o-- Month
    MonthlyBudget o-- Money
    LivingExpenseCategory o-- LivingExpenseCategoryId
    LivingExpenseCategory o-- UserGroupId
    LivingExpenseCategory o-- CategoryName
    LivingExpenseCategory o-- Description
    DailyTransaction o-- Money
    DailyLivingExpense o-- DailyLivingExpenseId
    DailyLivingExpense o-- LivingExpenseCategoryId
    DailyLivingExpense o-- Money
    DailyLivingExpense o-- Description
    DailyPersonalExpense o-- SequenceNumber
    DailyPersonalExpense o-- Money
    DailyPersonalExpense o-- Description
    DailyBudgetBalance o-- UserGroupId
    DailyBudgetBalance o-- Money
    FixedExpenseCategory o-- FixedExpenseCategoryId
    FixedExpenseCategory o-- UserGroupId
    FixedExpenseCategory o-- CategoryName
    FixedExpenseCategory o-- Description
    FixedExpenseCategory o-- Money
    FixedExpenseHistory o-- FixedExpenseHistoryId
    FixedExpenseHistory o-- FixedExpenseCategoryId
    FixedExpenseHistory o-- Year
    FixedExpenseHistory o-- Month
    FixedExpenseHistory o-- Money
    FixedExpenseHistory o-- Description
    MonthlySaving o-- Year
    MonthlySaving o-- Month
    MonthlySaving o-- Money
    MonthlySaving o-- Description

    %% エンティティ間の関係性
    User o-- UserGroup
    User o-- FinancialAccount
    User o-- DailyTransaction
    User o-- MonthlySaving

    UserGroup o-- GroupInvitation
    UserGroup o-- MonthlyBudget
    UserGroup o-- LivingExpenseCategory
    UserGroup o-- FixedExpenseCategory

    FinancialAccount o-- BalanceEditHistory
    FinancialAccount o-- DailyTransaction
    FinancialAccount o-- MonthlySaving

    DailyTransaction o-- DailyLivingExpense
    DailyTransaction o-- DailyPersonalExpense

    LivingExpenseCategory o-- DailyLivingExpense
    FixedExpenseCategory o-- FixedExpenseHistory

    %% サービス層の依存関係
    UserController ..> UserService
    UserGroupController ..> UserGroupService
    TransactionController ..> TransactionService
    BudgetController ..> BudgetService
    ExpenseController ..> ExpenseService
    AccountController ..> AccountService
    SavingController ..> SavingService

    UserService ..> UserRepository
    UserService ..> GroupInvitationRepository
    UserGroupService ..> UserGroupRepository
    UserGroupService ..> GroupInvitationRepository
    TransactionService ..> DailyTransactionRepository
    TransactionService ..> DailyLivingExpenseRepository
    TransactionService ..> DailyPersonalExpenseRepository
    BudgetService ..> MonthlyBudgetRepository
    BudgetService ..> DailyBudgetBalanceRepository
    ExpenseService ..> LivingExpenseCategoryRepository
    ExpenseService ..> FixedExpenseCategoryRepository
    ExpenseService ..> FixedExpenseHistoryRepository
    AccountService ..> FinancialAccountRepository
    AccountService ..> BalanceEditHistoryRepository
    SavingService ..> MonthlySavingRepository
```

## クラス設計の特徴

### エンティティクラス
- JPA/Hibernateアノテーションを使用したエンティティクラス
- ER図の各テーブルに対応

### リクエストクラス
- REST APIのリクエストボディを表現
- クライアントからサーバーへのデータ送信に使用
- バリデーションアノテーション対応

### サービスクラス
- ビジネスロジックを含む
- トランザクション管理
- 要件定義の各機能に対応

### コントローラークラス
- REST APIエンドポイントを提供
- HTTPリクエストのハンドリング
- レスポンス形式の統一

### リポジトリインターフェース
- データアクセス層
- Spring Data JPAを使用
- カスタムクエリメソッドを定義
