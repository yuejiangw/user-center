import type {DescriptionsProps} from 'antd';
import {Descriptions, Divider, Modal, Table, Tag} from 'antd';


interface UserProfileModalProps {
  isOpen: boolean,
  handleModalClose: () => void,
  plan: API.Plan | null,
}

const ViewPlanModal = ({isOpen, handleModalClose, plan}: UserProfileModalProps) => {
  console.log("Plan: ", plan);

  const descriptionItems: DescriptionsProps['items'] = [
    {
      key: 'plan-id',
      label: 'Plan ID',
      children: plan?.id
    },
    {
      key: 'plan-name',
      label: 'Plan Name',
      children: plan?.name
    },
    {
      key: 'course-direction',
      label: 'Course Direction',
      children: plan?.courseDirection
    },
    {
      key: 'sub-direction',
      label: 'Sub Direction',
      children: plan?.subDirection?.split(',').map(sd => <Tag key={sd}>{sd}</Tag>)
    }
  ]

  // Table columns for course stages
  const courseDetailColumns = [
    {
      title: 'Stage',
      dataIndex: 'stageName',
      key: 'stageName',
    },
    {
      title: 'Content',
      dataIndex: 'content',
      key: 'content',
      render: (_: any, record: API.CourseStage) => (
        <>
          {record.lessons.map((lesson: API.Lesson, index: number) => (
            <div key={index}>{lesson.content}</div>
          ))}
        </>
      ),
    },
    {
      title: 'Course Time',
      dataIndex: 'courseTime',
      key: 'courseTime',
      render: (_: any, record: API.CourseStage) => (
        <>
          {record.lessons.map((lesson: API.Lesson, index: number) => (
            <div key={index}>{lesson.courseTime}</div>
          ))}
        </>
      ),
    },
  ];

  return (
    <Modal
      title="Plan Details"
      open={isOpen}
      onCancel={handleModalClose}
      footer={null}
      width={1000} // 控制 Modal 的宽度
    >
      <Divider type={"horizontal"}/>
      <div className={'detail-section-title'}>计划概况</div>
      <Descriptions bordered layout={'vertical'}>
        {descriptionItems.map(item => (
          <Descriptions.Item key={item.key} label={item.label}>
            {item.children}
          </Descriptions.Item>
        ))}
        <Descriptions.Item label="Course Target" span={3}>
          {plan?.courseTarget}
        </Descriptions.Item>
        <Descriptions.Item label="Comment" span={3}>
          {plan?.comment}
        </Descriptions.Item>
      </Descriptions>

      <Divider type={"horizontal"}/>
      <div className={'detail-section-title'}>计划课表</div>
      <Table
        columns={courseDetailColumns}
        dataSource={plan?.courseDetail}
        rowKey="stageName"
        pagination={false}
        bordered={true}
      />
    </Modal>
  );
}

export default ViewPlanModal;
