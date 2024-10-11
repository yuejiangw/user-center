import {createPlan, deletePlan, searchPlans} from '@/services/ant-design-pro/api';
import {PlusOutlined} from '@ant-design/icons';
import type {ActionType, ProColumns} from '@ant-design/pro-components';
import {ProTable, TableDropdown} from '@ant-design/pro-components';
import {Button, Divider, message, Popconfirm, Tag} from 'antd';
import React, {useRef, useState} from 'react';
import {Footer} from "@/components";
import CreatePlanModal from "@/pages/Admin/PlanManage/CreatePlanModal";
import ViewPlanModal from "@/pages/Admin/PlanManage/ViewPlanModal";

const PlanManageTable = () => {
  const actionRef = useRef<ActionType>();
  const [isViewPlanModalOpen, setIsViewPlanModalOpen] = useState(false);
  const [isCreatePlanModalOpen, setIsCreatePlanModalOpen] = useState(false);
  const [currentRecord, setCurrentRecord] = useState<API.Plan | null>(null);

  const handleViewPlan = (record: API.Plan) => {
    setCurrentRecord(record);
    setIsViewPlanModalOpen(true);
  };

  const handleViewPlanModalClose = () => {
    setIsViewPlanModalOpen(false);
    setCurrentRecord(null);
  };

  const handleCreatePlan = () => {
    setIsCreatePlanModalOpen(true);
  }

  const handleCreatePlanModalClose = () => {
    setIsCreatePlanModalOpen(false);
  }

  const handleMenuSelect = (key: string, record: API.Plan, action) => {
    if (key === 'copy') {
      // 处理复制逻辑
      createPlan(record)
        .then(action?.reload())
        .finally(message.success('Plan successfully duplicated!'));
    } else if (key === 'edit') {
      // 处理编辑逻辑
      
    }
  }

  const columns: ProColumns<API.Plan>[] = [
    {
      dataIndex: 'id',
      valueType: 'indexBorder',
      width: 48,
    },
    {
      title: 'Name',
      dataIndex: 'name',
      copyable: true,
      ellipsis: true
    },
    {
      title: 'Status',
      dataIndex: 'isPublished',
      ellipsis: true,
      render: (_, record) => {
        const isPublished = record.isPublished;
        // 判断状态并返回对应的 Tag
        return isPublished === 0 ? (
          <Tag color="default">Unpublished</Tag>
        ) : (
          <Tag color="green">Published</Tag>
        );
      }
    },
    {
      title: 'Creator ID',
      dataIndex: 'creatorId',
      copyable: true
    },
    {
      title: 'Direction',
      dataIndex: 'courseDirection',
      copyable: true,
      ellipsis: true
    },
    {
      title: 'SubDirection',
      dataIndex: 'subDirection',
      render: (_, record) => {
        if (record.subDirection) {
          return (
            record.subDirection.split(',').map(d => <Tag key={d}>{d}</Tag>)
          )
        } else {
          return null;
        }
      },
      search: false,
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
      width: 150,
      fixed: "right",
      render: (text, record, _, action) => [
        <a href={'#'} key="view" onClick={() => handleViewPlan(record)}>
          View
        </a>,
        // 分隔符
        <Divider key={'divider'} type={'vertical'}/>,
        <TableDropdown
          key="actionGroup"
          onSelect={(key: string) => handleMenuSelect(key, record, action)}
          menus={[
            {key: 'copy', name: 'Copy'},
            {key: 'edit', name: 'Edit'},
            {
              key: 'delete',
              name: (
                <Popconfirm
                  title="Are you sure to delete this plan?"
                  onConfirm={async () => {
                    const success = await deletePlan({id: record.id});
                    if (success) {
                      message.success('教学计划删除成功');
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
        >
          Operations
        </TableDropdown>,
      ],
    },
  ];

  return (
    <>
      <ProTable<API.Plan>
        scroll={{x: 'max-content'}}
        columns={columns}
        actionRef={actionRef}
        cardBordered
        request={async (params, sort, filter) => {
          const planList = await searchPlans(params);
          return {
            data: planList,
          };
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
            onClick={handleCreatePlan}
            type="primary"
          >
            新建
          </Button>,
        ]}
      />
      <ViewPlanModal
        isOpen={isViewPlanModalOpen}
        handleModalClose={handleViewPlanModalClose}
        plan={currentRecord}
      />
      <CreatePlanModal isOpen={isCreatePlanModalOpen} handleModalClose={handleCreatePlanModalClose}/>
    </>
  );
};

const PlanManageForm = () => {
  return (
    <div>
      <PlanManageTable/>
      <Footer/>
    </div>
  )
}

export default PlanManageForm;
