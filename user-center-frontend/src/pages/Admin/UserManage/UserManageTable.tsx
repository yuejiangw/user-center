import { DEFAULT_AVATAR } from '@/common/constants';
import { searchUsers } from '@/services/ant-design-pro/api';
import { EllipsisOutlined, PlusOutlined } from '@ant-design/icons';
import type { ActionType, ProColumns } from '@ant-design/pro-components';
import { ProTable, TableDropdown } from '@ant-design/pro-components';
import { Button, Dropdown, Image } from 'antd';
import { useRef } from 'react';

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
      return <Image src={record.avatarUrl} fallback={DEFAULT_AVATAR} width={50} height={50} />;
    },
    ellipsis: true,
    search: false,
  },
  {
    title: 'Gender',
    dataIndex: 'gender',
    ellipsis: true,
    valueEnum: {
      0: { text: 'Female' },
      1: { text: 'Male' },
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
      0: { text: 'Active', status: 'Success' },
      1: { text: 'Deactivate', status: 'Default' },
    },
  },
  {
    title: 'Role',
    dataIndex: 'userRole',
    ellipsis: true,
    valueEnum: {
      0: { text: 'User', status: 'Default' },
      1: { text: 'Admin', status: 'Success' },
    },
  },
  {
    title: 'Create Time',
    dataIndex: 'createTime',
    valueType: 'dateTime',
    search: false,
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
      <a href={record.url} target="_blank" rel="noopener noreferrer" key="view">
        View
      </a>,
      <TableDropdown
        key="actionGroup"
        onSelect={() => action?.reload()}
        menus={[
          { key: 'copy', name: 'Copy' },
          { key: 'delete', name: 'Delete' },
        ]}
      />,
    ],
  },
];

const UserManageTable = () => {
  const actionRef = useRef<ActionType>();
  return (
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
      }}
      columnsState={{
        persistenceKey: 'pro-table-singe-demos',
        persistenceType: 'localStorage',
        defaultValue: {
          option: { fixed: 'right', disable: true },
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
          icon={<PlusOutlined />}
          onClick={() => {
            actionRef.current?.reload();
          }}
          type="primary"
        >
          新建
        </Button>,
        <Dropdown
          key="menu"
          menu={{
            items: [
              {
                label: '1st item',
                key: '1',
              },
              {
                label: '2nd item',
                key: '2',
              },
              {
                label: '3rd item',
                key: '3',
              },
            ],
          }}
        >
          <Button>
            <EllipsisOutlined />
          </Button>
        </Dropdown>,
      ]}
    />
  );
};

export default UserManageTable;
