# 家計簿アプリ クラス図

```mermaid
classDiagram
    %% エンティティクラス
    class User {
        -UserId id
        -Username username
        -UserGroupId userGroupId
        +getId() UserId
        +getUsername() Username
        +getUserGroupId() UserGroupId
    }

    class UserGroup {
        -UserGroupId id
        -GroupName groupName
        -Day monthStartDay
        -UserId createdByUserId
        +getId() UserGroupId
        +getGroupName() GroupName
        +getMonthStartDay() Day
        +getCreatedByUserId() UserId
    }

    class GroupInvitation {
        -UserGroupId userGroupId
        -UserId invitedUserId
        -UserId invitedByUserId
        -InvitationStatus status
        -LocalDateTime invitedAt
        -LocalDateTime respondedAt
        +getUserGroupId() UserGroupId
        +getInvitedUserId() UserId
        +getInvitedByUserId() UserId
        +getStatus() InvitationStatus
    }

    class FinancialAccount {
        -FinancialAccountId id
        -UserId userId
        -AccountName accountName
        -Money balance
        -Boolean isMainAccount
        +getId() FinancialAccountId
        +getUserId() UserId
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
        -UserId setByUserId
        +getUserGroupId() UserGroupId
        +getYear() Year
        +getMonth() Month
        +getBudgetAmount() Money
        +getSetByUserId() UserId
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
        -UserId userId
        -LocalDate transactionDate
        -Money income
        -Money totalExpense
        -Money personalExpense
        -FinancialAccountId financialAccountId
        +getUserId() UserId
        +getTransactionDate() LocalDate
        +getIncome() Money
        +getTotalExpense() Money
        +getPersonalExpense() Money
        +getFinancialAccountId() FinancialAccountId
    }

    class DailyLivingExpense {
        -DailyLivingExpenseId id
        -UserId userId
        -LocalDate transactionDate
        -LivingExpenseCategoryId livingExpenseCategoryId
        -Money amount
        -Description memo
        +getId() DailyLivingExpenseId
        +getUserId() UserId
        +getTransactionDate() LocalDate
        +getLivingExpenseCategoryId() LivingExpenseCategoryId
        +getAmount() Money
        +getMemo() Description
    }

    class DailyPersonalExpense {
        -UserId userId
        -LocalDate transactionDate
        -SequenceNumber sequenceNo
        -Money amount
        -Description description
        +getUserId() UserId
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
        -UserId userId
        -Year year
        -Month month
        -Money savingAmount
        -FinancialAccountId financialAccountId
        -Description memo
        +getUserId() UserId
        +getYear() Year
        +getMonth() Month
        +getSavingAmount() Money
        +getFinancialAccountId() FinancialAccountId
        +getMemo() Description
    }

    %% DTOクラス
    class UserRegistrationDto {
        -Username username
        -String password
        +getUsername() Username
        +getPassword() String
    }

    class UserLoginDto {
        -Username username
        -String password
        +getUsername() Username
        +getPassword() String
    }

    class DailyTransactionDto {
        -LocalDate transactionDate
        -Money income
        -List~DailyLivingExpenseDto~ livingExpenses
        -List~DailyPersonalExpenseDto~ personalExpenses
        +getTransactionDate() LocalDate
        +getIncome() Money
        +getLivingExpenses() List~DailyLivingExpenseDto~
        +getPersonalExpenses() List~DailyPersonalExpenseDto~
    }

    class DailyLivingExpenseDto {
        -Long categoryId
        -Money amount
        -Description memo
        +getCategoryId() Long
        +getAmount() Money
        +getMemo() Description
    }

    class DailyPersonalExpenseDto {
        -Money amount
        -Description description
        +getAmount() Money
        +getDescription() Description
    }

    class MonthlyBudgetDto {
        -Year year
        -Month month
        -Money budgetAmount
        +getYear() Year
        +getMonth() Month
        +getBudgetAmount() Money
    }

    class GroupInvitationDto {
        -Username invitedUsername
        +getInvitedUsername() Username
    }

    %% DTO間の関係性
    DailyTransactionDto o-- DailyLivingExpenseDto
    DailyTransactionDto o-- DailyPersonalExpenseDto

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

    class UserId {
        -Long value
        +getValue() Long
        +isValid() boolean
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
        +registerUser(UserRegistrationDto) User
        +authenticateUser(UserLoginDto) User
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
        +inviteUser(GroupInvitationDto) GroupInvitation
        +leaveGroup(Long) void
        +getGroupMembers(Long) List~User~
    }

    class TransactionService {
        -DailyTransactionRepository dailyTransactionRepository
        -DailyLivingExpenseRepository dailyLivingExpenseRepository
        -DailyPersonalExpenseRepository dailyPersonalExpenseRepository
        -BudgetService budgetService
        +recordDailyTransaction(DailyTransactionDto) DailyTransaction
        +calculateTotalExpense(List~DailyLivingExpenseDto~, List~DailyPersonalExpenseDto~, Integer) BigDecimal
        +getDailyTransaction(Long, LocalDate) DailyTransaction
        +updateDailyTransaction(Long, LocalDate, DailyTransactionDto) DailyTransaction
        +deleteDailyTransaction(Long, LocalDate) void
    }

    class BudgetService {
        -MonthlyBudgetRepository monthlyBudgetRepository
        -DailyBudgetBalanceRepository dailyBudgetBalanceRepository
        +setMonthlyBudget(MonthlyBudgetDto) MonthlyBudget
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
        +register(UserRegistrationDto) ResponseEntity~User~
        +login(UserLoginDto) ResponseEntity~String~
        +getCurrentUser() ResponseEntity~User~
        +logout() ResponseEntity~Void~
        +acceptGroupInvitation(Long) ResponseEntity~Void~
        +rejectGroupInvitation(Long) ResponseEntity~Void~
    }

    class UserGroupController {
        -UserGroupService userGroupService
        +createGroup(String) ResponseEntity~UserGroup~
        +inviteUser(GroupInvitationDto) ResponseEntity~GroupInvitation~
        +leaveGroup() ResponseEntity~Void~
        +getGroupMembers() ResponseEntity~List~User~~
    }

    class TransactionController {
        -TransactionService transactionService
        +recordTransaction(DailyTransactionDto) ResponseEntity~DailyTransaction~
        +getTransaction(LocalDate) ResponseEntity~DailyTransaction~
        +updateTransaction(LocalDate, DailyTransactionDto) ResponseEntity~DailyTransaction~
        +deleteTransaction(LocalDate) ResponseEntity~Void~
    }

    class BudgetController {
        -BudgetService budgetService
        +setMonthlyBudget(MonthlyBudgetDto) ResponseEntity~MonthlyBudget~
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
        +findByCreatedByUserId(Long) Optional~UserGroup~
    }

    class GroupInvitationRepository {
        <<interface>>
        +findByInvitedUserIdAndStatus(Long, String) List~GroupInvitation~
        +findByUserGroupIdAndInvitedUserId(Long, Long) Optional~GroupInvitation~
    }

    class FinancialAccountRepository {
        <<interface>>
        +findByUserId(Long) List~FinancialAccount~
        +findByUserIdAndIsMainAccount(Long, Boolean) Optional~FinancialAccount~
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
        +findByUserIdAndTransactionDate(Long, LocalDate) Optional~DailyTransaction~
        +findByUserIdAndTransactionDateBetween(Long, LocalDate, LocalDate) List~DailyTransaction~
    }

    class DailyLivingExpenseRepository {
        <<interface>>
        +findByUserIdAndTransactionDate(Long, LocalDate) List~DailyLivingExpense~
        +findByUserIdAndTransactionDateBetween(Long, LocalDate, LocalDate) List~DailyLivingExpense~
    }

    class DailyPersonalExpenseRepository {
        <<interface>>
        +findByUserIdAndTransactionDate(Long, LocalDate) List~DailyPersonalExpense~
        +findByUserIdAndTransactionDateBetween(Long, LocalDate, LocalDate) List~DailyPersonalExpense~
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
        +findByUserIdAndYearAndMonth(Long, Integer, Integer) Optional~MonthlySaving~
        +findByUserIdAndYear(Long, Integer) List~MonthlySaving~
    }

    %% 値オブジェクトとの関係性
    User o-- UserId
    User o-- Username
    User o-- UserGroupId
    UserGroup o-- UserGroupId
    UserGroup o-- GroupName
    UserGroup o-- Day
    UserGroup o-- UserId
    GroupInvitation o-- UserGroupId
    GroupInvitation o-- UserId
    GroupInvitation o-- InvitationStatus
    FinancialAccount o-- FinancialAccountId
    FinancialAccount o-- UserId
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
    MonthlyBudget o-- UserId
    LivingExpenseCategory o-- LivingExpenseCategoryId
    LivingExpenseCategory o-- UserGroupId
    LivingExpenseCategory o-- CategoryName
    LivingExpenseCategory o-- Description
    DailyTransaction o-- UserId
    DailyTransaction o-- Money
    DailyLivingExpense o-- DailyLivingExpenseId
    DailyLivingExpense o-- UserId
    DailyLivingExpense o-- LivingExpenseCategoryId
    DailyLivingExpense o-- Money
    DailyLivingExpense o-- Description
    DailyPersonalExpense o-- UserId
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
    MonthlySaving o-- UserId
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

### DTOクラス
- データ転送用オブジェクト
- クライアントとサーバー間のデータ交換に使用
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
