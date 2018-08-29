create index IX_5BA58D71 on registration_registration (field2);
create index IX_D00D4939 on registration_registration (uuid_[$COLUMN_LENGTH:75$], companyId);
create unique index IX_CC98C9FB on registration_registration (uuid_[$COLUMN_LENGTH:75$], groupId);