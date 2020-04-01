USE [master]
GO
/****** Object:  Database [MobileProject]    Script Date: 31/3/2020 8:33:03 AM ******/
CREATE DATABASE [MobileProject]
 CONTAINMENT = NONE
 ON  PRIMARY 
( NAME = N'MobileProject', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL14.SQLEXPRESS\MSSQL\DATA\MobileProject.mdf' , SIZE = 8192KB , MAXSIZE = UNLIMITED, FILEGROWTH = 65536KB )
 LOG ON 
( NAME = N'MobileProject_log', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL14.SQLEXPRESS\MSSQL\DATA\MobileProject_log.ldf' , SIZE = 8192KB , MAXSIZE = 2048GB , FILEGROWTH = 65536KB )
GO
ALTER DATABASE [MobileProject] SET COMPATIBILITY_LEVEL = 140
GO
IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [MobileProject].[dbo].[sp_fulltext_database] @action = 'enable'
end
GO
ALTER DATABASE [MobileProject] SET ANSI_NULL_DEFAULT OFF 
GO
ALTER DATABASE [MobileProject] SET ANSI_NULLS OFF 
GO
ALTER DATABASE [MobileProject] SET ANSI_PADDING OFF 
GO
ALTER DATABASE [MobileProject] SET ANSI_WARNINGS OFF 
GO
ALTER DATABASE [MobileProject] SET ARITHABORT OFF 
GO
ALTER DATABASE [MobileProject] SET AUTO_CLOSE OFF 
GO
ALTER DATABASE [MobileProject] SET AUTO_SHRINK OFF 
GO
ALTER DATABASE [MobileProject] SET AUTO_UPDATE_STATISTICS ON 
GO
ALTER DATABASE [MobileProject] SET CURSOR_CLOSE_ON_COMMIT OFF 
GO
ALTER DATABASE [MobileProject] SET CURSOR_DEFAULT  GLOBAL 
GO
ALTER DATABASE [MobileProject] SET CONCAT_NULL_YIELDS_NULL OFF 
GO
ALTER DATABASE [MobileProject] SET NUMERIC_ROUNDABORT OFF 
GO
ALTER DATABASE [MobileProject] SET QUOTED_IDENTIFIER OFF 
GO
ALTER DATABASE [MobileProject] SET RECURSIVE_TRIGGERS OFF 
GO
ALTER DATABASE [MobileProject] SET  DISABLE_BROKER 
GO
ALTER DATABASE [MobileProject] SET AUTO_UPDATE_STATISTICS_ASYNC OFF 
GO
ALTER DATABASE [MobileProject] SET DATE_CORRELATION_OPTIMIZATION OFF 
GO
ALTER DATABASE [MobileProject] SET TRUSTWORTHY OFF 
GO
ALTER DATABASE [MobileProject] SET ALLOW_SNAPSHOT_ISOLATION OFF 
GO
ALTER DATABASE [MobileProject] SET PARAMETERIZATION SIMPLE 
GO
ALTER DATABASE [MobileProject] SET READ_COMMITTED_SNAPSHOT OFF 
GO
ALTER DATABASE [MobileProject] SET HONOR_BROKER_PRIORITY OFF 
GO
ALTER DATABASE [MobileProject] SET RECOVERY SIMPLE 
GO
ALTER DATABASE [MobileProject] SET  MULTI_USER 
GO
ALTER DATABASE [MobileProject] SET PAGE_VERIFY CHECKSUM  
GO
ALTER DATABASE [MobileProject] SET DB_CHAINING OFF 
GO
ALTER DATABASE [MobileProject] SET FILESTREAM( NON_TRANSACTED_ACCESS = OFF ) 
GO
ALTER DATABASE [MobileProject] SET TARGET_RECOVERY_TIME = 60 SECONDS 
GO
ALTER DATABASE [MobileProject] SET DELAYED_DURABILITY = DISABLED 
GO
ALTER DATABASE [MobileProject] SET QUERY_STORE = OFF
GO
USE [MobileProject]
GO
/****** Object:  Table [dbo].[GroupInfo]    Script Date: 31/3/2020 8:33:04 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[GroupInfo](
	[GroupID] [int] IDENTITY(1,1) NOT NULL,
	[GroupName] [nvarchar](100) NULL,
	[ManagerID] [int] NULL,
 CONSTRAINT [PK_GroupInfo] PRIMARY KEY CLUSTERED 
(
	[GroupID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[GroupUser]    Script Date: 31/3/2020 8:33:04 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[GroupUser](
	[GroupID] [int] NULL,
	[UserID] [int] NULL
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[PersonalTaskInfo]    Script Date: 31/3/2020 8:33:04 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[PersonalTaskInfo](
	[TaskID] [int] IDENTITY(1,1) NOT NULL,
	[TaskName] [nvarchar](50) NULL,
	[TaskDescription] [nvarchar](100) NULL,
	[TaskHandlingContent] [nvarchar](100) NULL,
	[Status] [nvarchar](50) NULL,
	[Creator] [nvarchar](50) NULL,
	[CreatorRole] [nvarchar](20) NULL,
	[TaskHandler] [nvarchar](50) NULL,
	[TaskConfirmation] [nvarchar](30) NULL,
	[ConfirmationImage] [nvarchar](max) NULL,
 CONSTRAINT [PK_PersonalWork] PRIMARY KEY CLUSTERED 
(
	[TaskID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[PersonalTaskManager]    Script Date: 31/3/2020 8:33:04 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[PersonalTaskManager](
	[TaskID] [int] NOT NULL,
	[ManagerComment] [nvarchar](100) NULL,
	[ManagerMark] [int] NULL,
	[ManagerCommentBeginTime] [date] NULL,
 CONSTRAINT [PK_PersonalWorkManager] PRIMARY KEY CLUSTERED 
(
	[TaskID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[PersonalTaskTime]    Script Date: 31/3/2020 8:33:04 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[PersonalTaskTime](
	[TaskID] [int] NOT NULL,
	[TimeBegin] [date] NULL,
	[TimeFinish] [date] NULL,
	[TimeCreated] [date] NULL,
 CONSTRAINT [PK_PersonalWorkTime] PRIMARY KEY CLUSTERED 
(
	[TaskID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[UserInfo]    Script Date: 31/3/2020 8:33:04 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[UserInfo](
	[UserID] [int] IDENTITY(1,1) NOT NULL,
	[Username] [nvarchar](50) NOT NULL,
	[Password] [nvarchar](50) NOT NULL,
	[FullName] [nvarchar](100) NULL,
	[Role] [nvarchar](20) NOT NULL,
 CONSTRAINT [PK_User] PRIMARY KEY CLUSTERED 
(
	[UserID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET IDENTITY_INSERT [dbo].[GroupInfo] ON 

INSERT [dbo].[GroupInfo] ([GroupID], [GroupName], [ManagerID]) VALUES (9, N'gr1', 2)
INSERT [dbo].[GroupInfo] ([GroupID], [GroupName], [ManagerID]) VALUES (12, N'gr2', -1)
INSERT [dbo].[GroupInfo] ([GroupID], [GroupName], [ManagerID]) VALUES (14, N'Group Test', 13)
SET IDENTITY_INSERT [dbo].[GroupInfo] OFF
INSERT [dbo].[GroupUser] ([GroupID], [UserID]) VALUES (9, 11)
INSERT [dbo].[GroupUser] ([GroupID], [UserID]) VALUES (14, 14)
INSERT [dbo].[GroupUser] ([GroupID], [UserID]) VALUES (9, 4)
INSERT [dbo].[GroupUser] ([GroupID], [UserID]) VALUES (12, 1)
SET IDENTITY_INSERT [dbo].[PersonalTaskInfo] ON 

INSERT [dbo].[PersonalTaskInfo] ([TaskID], [TaskName], [TaskDescription], [TaskHandlingContent], [Status], [Creator], [CreatorRole], [TaskHandler], [TaskConfirmation], [ConfirmationImage]) VALUES (1, N'task1', N'taskdesc1', N'hc1', N'Finished', N'user', N'User', N'user', N'Done', NULL)
INSERT [dbo].[PersonalTaskInfo] ([TaskID], [TaskName], [TaskDescription], [TaskHandlingContent], [Status], [Creator], [CreatorRole], [TaskHandler], [TaskConfirmation], [ConfirmationImage]) VALUES (4, N'task4edit', N'taskdesc4edit', N'hc4edit', N'Overdue', N'manager', N'User', N'user', N'Done', NULL)
INSERT [dbo].[PersonalTaskInfo] ([TaskID], [TaskName], [TaskDescription], [TaskHandlingContent], [Status], [Creator], [CreatorRole], [TaskHandler], [TaskConfirmation], [ConfirmationImage]) VALUES (5, N'task5', N'taskdesc5', N'hc5', N'Not started', N'user2', N'User', NULL, N'Not Confirmed', NULL)
INSERT [dbo].[PersonalTaskInfo] ([TaskID], [TaskName], [TaskDescription], [TaskHandlingContent], [Status], [Creator], [CreatorRole], [TaskHandler], [TaskConfirmation], [ConfirmationImage]) VALUES (7, N'taskManager1', N'descmanga', N'hcmanga', N'Finished', N'manager', N'Manager', N'user', N'Not Confirmed', NULL)
INSERT [dbo].[PersonalTaskInfo] ([TaskID], [TaskName], [TaskDescription], [TaskHandlingContent], [Status], [Creator], [CreatorRole], [TaskHandler], [TaskConfirmation], [ConfirmationImage]) VALUES (8, N'newy', N'aa', N'o', N'In progress', N'user', N'User', N'user', N'Not Confirmed', NULL)
INSERT [dbo].[PersonalTaskInfo] ([TaskID], [TaskName], [TaskDescription], [TaskHandlingContent], [Status], [Creator], [CreatorRole], [TaskHandler], [TaskConfirmation], [ConfirmationImage]) VALUES (10, N'oo', N'aa', N'ew', N'In progress', N'user', N'User', N'user', N'Not Confirmed', NULL)
INSERT [dbo].[PersonalTaskInfo] ([TaskID], [TaskName], [TaskDescription], [TaskHandlingContent], [Status], [Creator], [CreatorRole], [TaskHandler], [TaskConfirmation], [ConfirmationImage]) VALUES (11, N'taskMan2', N'we', N'aa', N'Not started', N'manager', N'Manager', N'manager', N'Not Confirmed', NULL)
INSERT [dbo].[PersonalTaskInfo] ([TaskID], [TaskName], [TaskDescription], [TaskHandlingContent], [Status], [Creator], [CreatorRole], [TaskHandler], [TaskConfirmation], [ConfirmationImage]) VALUES (12, N'taskAa', N'aa', N'qwerty', N'Finished', N'user', N'User', N'user', N'Not Confirmed', NULL)
INSERT [dbo].[PersonalTaskInfo] ([TaskID], [TaskName], [TaskDescription], [TaskHandlingContent], [Status], [Creator], [CreatorRole], [TaskHandler], [TaskConfirmation], [ConfirmationImage]) VALUES (15, N'task2', N'qq', N'wq', N'Finished', N'user2', N'User', N'user2', N'Accepted', N'3681d35b-aac5-48a2-821e-05763bc8e974')
INSERT [dbo].[PersonalTaskInfo] ([TaskID], [TaskName], [TaskDescription], [TaskHandlingContent], [Status], [Creator], [CreatorRole], [TaskHandler], [TaskConfirmation], [ConfirmationImage]) VALUES (18, N'task2.2', N'rr', N'tt', N'In progress', N'user2', N'User', N'user2', N'Not Confirmed', N'7bab1304-f627-4a60-9a58-10ed27ad80a8')
INSERT [dbo].[PersonalTaskInfo] ([TaskID], [TaskName], [TaskDescription], [TaskHandlingContent], [Status], [Creator], [CreatorRole], [TaskHandler], [TaskConfirmation], [ConfirmationImage]) VALUES (19, N'Task User', N'aaa', N'aaa', N'Finished', N'testuser', N'User', N'testuser', N'Accepted', N'19c2fcfe-ac5f-4304-9b34-24b60f0179c6')
INSERT [dbo].[PersonalTaskInfo] ([TaskID], [TaskName], [TaskDescription], [TaskHandlingContent], [Status], [Creator], [CreatorRole], [TaskHandler], [TaskConfirmation], [ConfirmationImage]) VALUES (20, N'Task Manager', N'ww', N'ww', N'Finished', N'testmanager', N'Manager', N'testuser', N'Not Confirmed', N'f64d22de-0e39-4350-a49e-b8ae94bf969d')
SET IDENTITY_INSERT [dbo].[PersonalTaskInfo] OFF
INSERT [dbo].[PersonalTaskManager] ([TaskID], [ManagerComment], [ManagerMark], [ManagerCommentBeginTime]) VALUES (15, N'average', 5, CAST(N'2020-03-29' AS Date))
INSERT [dbo].[PersonalTaskManager] ([TaskID], [ManagerComment], [ManagerMark], [ManagerCommentBeginTime]) VALUES (18, N'good', 10, CAST(N'2020-03-29' AS Date))
INSERT [dbo].[PersonalTaskManager] ([TaskID], [ManagerComment], [ManagerMark], [ManagerCommentBeginTime]) VALUES (19, N' ', 1, CAST(N'2020-03-31' AS Date))
INSERT [dbo].[PersonalTaskManager] ([TaskID], [ManagerComment], [ManagerMark], [ManagerCommentBeginTime]) VALUES (20, N'good', 10, CAST(N'2020-03-31' AS Date))
INSERT [dbo].[PersonalTaskTime] ([TaskID], [TimeBegin], [TimeFinish], [TimeCreated]) VALUES (1, CAST(N'2021-10-04' AS Date), CAST(N'2021-10-04' AS Date), CAST(N'2021-10-04' AS Date))
INSERT [dbo].[PersonalTaskTime] ([TaskID], [TimeBegin], [TimeFinish], [TimeCreated]) VALUES (4, CAST(N'2020-03-22' AS Date), CAST(N'2020-03-23' AS Date), CAST(N'2020-03-22' AS Date))
INSERT [dbo].[PersonalTaskTime] ([TaskID], [TimeBegin], [TimeFinish], [TimeCreated]) VALUES (5, CAST(N'2020-04-01' AS Date), CAST(N'2020-04-30' AS Date), CAST(N'2020-03-23' AS Date))
INSERT [dbo].[PersonalTaskTime] ([TaskID], [TimeBegin], [TimeFinish], [TimeCreated]) VALUES (7, CAST(N'2020-03-23' AS Date), CAST(N'2020-03-26' AS Date), CAST(N'2020-03-23' AS Date))
INSERT [dbo].[PersonalTaskTime] ([TaskID], [TimeBegin], [TimeFinish], [TimeCreated]) VALUES (8, CAST(N'2020-03-25' AS Date), CAST(N'2020-03-26' AS Date), CAST(N'2020-03-25' AS Date))
INSERT [dbo].[PersonalTaskTime] ([TaskID], [TimeBegin], [TimeFinish], [TimeCreated]) VALUES (10, CAST(N'2020-03-26' AS Date), CAST(N'2020-03-26' AS Date), CAST(N'2020-03-26' AS Date))
INSERT [dbo].[PersonalTaskTime] ([TaskID], [TimeBegin], [TimeFinish], [TimeCreated]) VALUES (11, CAST(N'2020-03-26' AS Date), CAST(N'2020-03-28' AS Date), CAST(N'2020-03-26' AS Date))
INSERT [dbo].[PersonalTaskTime] ([TaskID], [TimeBegin], [TimeFinish], [TimeCreated]) VALUES (12, CAST(N'2020-03-28' AS Date), CAST(N'2020-04-23' AS Date), CAST(N'2020-03-28' AS Date))
INSERT [dbo].[PersonalTaskTime] ([TaskID], [TimeBegin], [TimeFinish], [TimeCreated]) VALUES (15, CAST(N'2020-03-29' AS Date), CAST(N'2020-03-31' AS Date), CAST(N'2020-03-29' AS Date))
INSERT [dbo].[PersonalTaskTime] ([TaskID], [TimeBegin], [TimeFinish], [TimeCreated]) VALUES (18, CAST(N'2020-03-29' AS Date), CAST(N'2020-03-31' AS Date), CAST(N'2020-03-29' AS Date))
INSERT [dbo].[PersonalTaskTime] ([TaskID], [TimeBegin], [TimeFinish], [TimeCreated]) VALUES (19, CAST(N'2020-03-31' AS Date), CAST(N'2020-04-02' AS Date), CAST(N'2020-03-31' AS Date))
INSERT [dbo].[PersonalTaskTime] ([TaskID], [TimeBegin], [TimeFinish], [TimeCreated]) VALUES (20, CAST(N'2020-03-31' AS Date), CAST(N'2020-04-02' AS Date), CAST(N'2020-03-31' AS Date))
SET IDENTITY_INSERT [dbo].[UserInfo] ON 

INSERT [dbo].[UserInfo] ([UserID], [Username], [Password], [FullName], [Role]) VALUES (1, N'user', N'123', N'Alpha', N'User')
INSERT [dbo].[UserInfo] ([UserID], [Username], [Password], [FullName], [Role]) VALUES (2, N'manager', N'1', N'Beta', N'Manager')
INSERT [dbo].[UserInfo] ([UserID], [Username], [Password], [FullName], [Role]) VALUES (3, N'admin', N'111', N'Charlie', N'Admin')
INSERT [dbo].[UserInfo] ([UserID], [Username], [Password], [FullName], [Role]) VALUES (4, N'user2', N'123', N'Alpino', N'User')
INSERT [dbo].[UserInfo] ([UserID], [Username], [Password], [FullName], [Role]) VALUES (5, N'manager2', N'1', N'Betadyne', N'Manager')
INSERT [dbo].[UserInfo] ([UserID], [Username], [Password], [FullName], [Role]) VALUES (9, N'anon', N'123', N'Anon', N'User')
INSERT [dbo].[UserInfo] ([UserID], [Username], [Password], [FullName], [Role]) VALUES (11, N'amon', N'123', N'Amon', N'User')
INSERT [dbo].[UserInfo] ([UserID], [Username], [Password], [FullName], [Role]) VALUES (13, N'testmanager', N'123', N'Test Manager', N'Manager')
INSERT [dbo].[UserInfo] ([UserID], [Username], [Password], [FullName], [Role]) VALUES (14, N'testuser', N'123', N'Test User', N'User')
SET IDENTITY_INSERT [dbo].[UserInfo] OFF
SET ANSI_PADDING ON
GO
/****** Object:  Index [UK_Username]    Script Date: 31/3/2020 8:33:04 AM ******/
ALTER TABLE [dbo].[UserInfo] ADD  CONSTRAINT [UK_Username] UNIQUE NONCLUSTERED 
(
	[Username] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
ALTER TABLE [dbo].[GroupUser]  WITH CHECK ADD  CONSTRAINT [FK_GroupUser_GroupInfo] FOREIGN KEY([GroupID])
REFERENCES [dbo].[GroupInfo] ([GroupID])
ON UPDATE CASCADE
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[GroupUser] CHECK CONSTRAINT [FK_GroupUser_GroupInfo]
GO
ALTER TABLE [dbo].[GroupUser]  WITH CHECK ADD  CONSTRAINT [FK_GroupUser_UserInfo] FOREIGN KEY([UserID])
REFERENCES [dbo].[UserInfo] ([UserID])
ON UPDATE CASCADE
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[GroupUser] CHECK CONSTRAINT [FK_GroupUser_UserInfo]
GO
ALTER TABLE [dbo].[PersonalTaskInfo]  WITH CHECK ADD  CONSTRAINT [FK_PersonalTaskInfo_UserInfo] FOREIGN KEY([TaskHandler])
REFERENCES [dbo].[UserInfo] ([Username])
GO
ALTER TABLE [dbo].[PersonalTaskInfo] CHECK CONSTRAINT [FK_PersonalTaskInfo_UserInfo]
GO
ALTER TABLE [dbo].[PersonalTaskInfo]  WITH CHECK ADD  CONSTRAINT [FK_PersonalWorkInfo_User] FOREIGN KEY([Creator])
REFERENCES [dbo].[UserInfo] ([Username])
GO
ALTER TABLE [dbo].[PersonalTaskInfo] CHECK CONSTRAINT [FK_PersonalWorkInfo_User]
GO
ALTER TABLE [dbo].[PersonalTaskManager]  WITH CHECK ADD  CONSTRAINT [FK_PersonalWorkManager_PersonalWorkInfo] FOREIGN KEY([TaskID])
REFERENCES [dbo].[PersonalTaskInfo] ([TaskID])
ON UPDATE CASCADE
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[PersonalTaskManager] CHECK CONSTRAINT [FK_PersonalWorkManager_PersonalWorkInfo]
GO
ALTER TABLE [dbo].[PersonalTaskTime]  WITH CHECK ADD  CONSTRAINT [FK_PersonalWorkTime_PersonalWorkInfo] FOREIGN KEY([TaskID])
REFERENCES [dbo].[PersonalTaskInfo] ([TaskID])
ON UPDATE CASCADE
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[PersonalTaskTime] CHECK CONSTRAINT [FK_PersonalWorkTime_PersonalWorkInfo]
GO
USE [master]
GO
ALTER DATABASE [MobileProject] SET  READ_WRITE 
GO
