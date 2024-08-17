import { EllipsisOutlined, PlusOutlined } from '@ant-design/icons';
import type { ActionType, ProColumns } from '@ant-design/pro-components';
import { ProTable, TableDropdown } from '@ant-design/pro-components';
import {Button, Dropdown, Image} from 'antd';
import { useRef } from 'react';
import {searchUsers} from "@/services/ant-design-pro/api";
import {DEFAULT_AVATAR} from "@/common/constants";

const columns: ProColumns<API.CurrentUser>[] = [
  {
    dataIndex: 'id',
    valueType: 'indexBorder',
    width: 48,
  },
  {
    title: 'User Name',
    dataIndex: 'username',
    copyable: true,
    ellipsis: true,
    tooltip: 'tooltip example, will ellipsis automatically if the title is too long',
  },
  {
    title: 'User Account',
    dataIndex: 'userAccount',
    copyable: true,
    ellipsis: true,
  },
  {
    title: 'User Avatar',
    dataIndex: 'avatarUrl',
    render: (_, record) => {
      return <Image src={record.avatarUrl} fallback={DEFAULT_AVATAR} width={50} height={50} />
    },
    ellipsis: true,
  },
  {
    title: 'Gender',
    dataIndex: 'gender',
    ellipsis: true,
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
    title: 'User Status',
    dataIndex: 'userStatus',
    ellipsis: true,
  },
  {
    title: 'User Role',
    dataIndex: 'userRole',
    ellipsis: true,
    valueEnum: {
      0: {text: 'Normal User', status: 'Default'},
      1: {text: 'Admin', status: 'Success'}
    }
  },
  {
    title: 'Create Time',
    dataIndex: 'createTime',
    valueType: 'dateTime',
  },
  {
    title: '操作',
    valueType: 'option',
    key: 'option',
    render: (text, record, _, action) => [
      <a
        key="editable"
        onClick={() => {
          action?.startEditable?.(record.id);
        }}
      >
        编辑
      </a>,
      <a href={record.url} target="_blank" rel="noopener noreferrer" key="view">
        查看
      </a>,
      <TableDropdown
        key="actionGroup"
        onSelect={() => action?.reload()}
        menus={[
          { key: 'copy', name: '复制' },
          { key: 'delete', name: '删除' },
        ]}
      />,
    ],
  },
];

const Table = () => {
  const actionRef = useRef<ActionType>();
  return (
    <ProTable<API.CurrentUser>
      columns={columns}
      actionRef={actionRef}
      cardBordered
      request={async (params, sort, filter) => {
        console.log(sort, filter);
        const userList = await searchUsers();
        console.log(userList);
        return {
          data: userList
        }
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
      headerTitle="高级表格"
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

export default Table;
