import {DEFAULT_AVATAR} from '@/common/constants';
import {deleteUser, searchUsers, updateUser} from '@/services/ant-design-pro/api';
import {PlusOutlined} from '@ant-design/icons';
import type {ActionType, ProColumns} from '@ant-design/pro-components';
import {ProTable, TableDropdown} from '@ant-design/pro-components';
import {Button, Image, message, Popconfirm} from 'antd';
import React, {useRef, useState} from 'react';
import {Footer} from "@/components";
import UserProfileModal from "@/pages/Admin/UserManage/UserProfileModal";
import CreateUserModal from "@/pages/Admin/UserManage/CreateUserModal";

const UserManageTable = () => {
  const actionRef = useRef<ActionType>();
  const [isUserProfileModalOpen, setIsUserProfileModalOpen] = useState(false);
  const [isCreateUserModalOpen, setIsCreateUserModalOpen] = useState(false);
  const [currentRecord, setCurrentRecord] = useState<API.CurrentUser | null>(null);

  const handleViewProfile = (record: API.CurrentUser) => {
    setCurrentRecord(record);
    setIsUserProfileModalOpen(true);
  };

  const handleUserProfileModalClose = () => {
    setIsUserProfileModalOpen(false);
    setCurrentRecord(null);
  };

  const handleCreateUser = () => {
    setIsCreateUserModalOpen(true);
  }

  const handleCreateUserModalClose = () => {
    setIsCreateUserModalOpen(false);
  }

  const handleMenuSelect = (key: string, record: API.CurrentUser) => {
    if (key === 'copy') {
      // 处理复制逻辑
      navigator.clipboard.writeText(JSON.stringify(record));
      message.success('User information copied to clipboard!');
    }
  }

  const columns: ProColumns<API.CurrentUser>[] = [
    {
      dataIndex: 'id',
      valueType: 'indexBorder',
      width: 48,
    },
    {
      title: 'Username',
      dataIndex: 'username',
      copyable: true,
      ellipsis: true,
      tooltip: 'tooltip example, will ellipsis automatically if the title is too long',
    },
    {
      title: 'Account',
      dataIndex: 'userAccount',
      copyable: true,
      ellipsis: true,
    },
    {
      title: 'Avatar',
      dataIndex: 'avatarUrl',
      render: (_, record) => {
        return <Image src={record.avatarUrl} fallback={DEFAULT_AVATAR} width={50} height={50}/>;
      },
      ellipsis: true,
      search: false,
    },
    {
      title: 'Gender',
      dataIndex: 'gender',
      ellipsis: true,
      valueEnum: {
        0: {text: 'Female'},
        1: {text: 'Male'},
      },
    },
    {
      title: 'Phone',
      dataIndex: 'phone',
      copyable: true,
      ellipsis: true,
    },
    {
      title: 'Email',
      dataIndex: 'email',
      copyable: true,
      ellipsis: true,
    },
    {
      title: 'Status',
      dataIndex: 'userStatus',
      ellipsis: true,
      valueEnum: {
        0: {text: 'Active', status: 'Success'},
        1: {text: 'Deactivate', status: 'Default'},
      },
    },
    {
      title: 'Role',
      dataIndex: 'userRole',
      ellipsis: true,
      valueEnum: {
        0: {text: 'User', status: 'Default'},
        1: {text: 'Admin', status: 'Success'},
      },
    },
    {
      title: 'Create Time',
      dataIndex: 'createTime',
      valueType: 'dateTime',
      search: false,
      editable: false
    },
    {
      title: 'Operations',
      valueType: 'option',
      key: 'option',
      render: (text, record, _, action) => [
        <a
          key="editable"
          onClick={() => {
            action?.startEditable?.(record.id);
          }}
        >
          Edit
        </a>,
        <a href={'#'} key="view" onClick={() => handleViewProfile(record)}>
          View
        </a>,
        <TableDropdown
          key="actionGroup"
          onSelect={(key: string) => handleMenuSelect(key, record)}
          menus={[
            {key: 'copy', name: 'Copy'},
            {
              key: 'delete',
              name: (
                <Popconfirm
                  title="Are you sure to delete this user?"
                  onConfirm={async () => {
                    const success = await deleteUser({id: record.id}); // 假设这是你的删除用户的 API
                    if (success) {
                      message.success('用户删除成功');
                      action?.reload(); // 刷新表格数据
                    } else {
                      message.error('删除失败，请重试');
                    }
                  }}
                  okText="Yes"
                  cancelText="No"
                >
                  <span style={{cursor: 'pointer', color: 'red'}}>Delete</span>
                </Popconfirm>
              )
            },
          ]}
        />,
      ],
    },
  ];

  return (
    <>
      <ProTable<API.CurrentUser>
        columns={columns}
        actionRef={actionRef}
        cardBordered
        request={async (params, sort, filter) => {
          const userList = await searchUsers(params);
          return {
            data: userList,
          };
        }}
        editable={{
          type: 'multiple',
          onSave: async (key, row) => {
            await updateUser(row);
            message.success(`Update user ${row.username} successful`)
          },
          onDelete: async (key, row) => {
            await deleteUser({id: row.id});
            message.success(`Delete user ${row.username} successful`)
          }
        }}
        columnsState={{
          persistenceKey: 'pro-table-singe-demos',
          persistenceType: 'localStorage',
          defaultValue: {
            option: {fixed: 'right', disable: true},
          },
          onChange(value) {
            console.log('value: ', value);
          },
        }}
        rowKey="id"
        search={{
          labelWidth: 'auto',
        }}
        options={{
          setting: {
            listsHeight: 400,
          },
        }}
        form={{
          // 由于配置了 transform，提交的参数与定义的不同这里需要转化一下
          syncToUrl: (values, type) => {
            if (type === 'get') {
              return {
                ...values,
                created_at: [values.startTime, values.endTime],
              };
            }
            return values;
          },
        }}
        pagination={{
          pageSize: 5,
          onChange: (page) => console.log(page),
        }}
        dateFormatter="string"
        headerTitle="Users"
        toolBarRender={() => [
          <Button
            key="button"
            icon={<PlusOutlined/>}
            onClick={handleCreateUser}
            type="primary"
          >
            新建
          </Button>,
        ]}
      />
      <UserProfileModal
        isOpen={isUserProfileModalOpen}
        handleModalClose={handleUserProfileModalClose}
        currentRecord={currentRecord}
      />
      <CreateUserModal isOpen={isCreateUserModalOpen} handleModalClose={handleCreateUserModalClose}/>
    </>
  );
};

const UserManageForm = () => {
  return (
    <div>
      <UserManageTable/>
      <Footer/>
    </div>
  )
}

export default UserManageForm;
