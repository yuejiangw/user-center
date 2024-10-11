import {message, Modal, Space} from 'antd';
import {createPlan} from "@/services/ant-design-pro/api";
import {ProForm, ProFormDateTimePicker, ProFormList} from "@ant-design/pro-form";
import {ProFormText} from "@ant-design/pro-components";


interface CreateUserModalProps {
  isOpen: boolean,
  handleModalClose: () => void,
}

const CreatePlanModal = ({isOpen, handleModalClose}: CreateUserModalProps) => {
  const handleSubmit = async (values: API.PlanCreateParams) => {
    console.log(values);
    const success = await createPlan(values);
    if (success) {
      message.success('课程计划创建成功');
    } else {
      message.error('课程计划创建失败，请重试');
    }
  };

  return (
    <Modal
      title="Create Plan"
      open={isOpen}
      onCancel={handleModalClose}
      footer={null}
    >
      <ProForm
        layout="vertical"
        submitter={{
          render: (_, dom) => {
            return (
              <div style={{textAlign: 'right', marginRight: '10px'}}>
                <Space>{dom}</Space>
              </div>
            );
          },
        }}
        onFinish={handleSubmit}
      >
        <ProFormText name="name" label="Plan Name" placeholder="Enter the plan name" rules={[{required: true}]}/>

        <ProFormList
          name="courseDetail"
          label="Course Stages"
          creatorButtonProps={{position: 'bottom'}}
          itemRender={({listDom, action}) => (
            <div style={{marginBottom: 8}}>
              {listDom}
              {action}
            </div>
          )}
        >
          <ProFormText name="stageName" label="Stage Name" placeholder="Enter stage name" rules={[{required: true}]}/>
          <ProFormList
            name="lessons"
            label="Lessons"
            creatorButtonProps={{position: 'bottom'}}
            itemRender={({listDom, action}) => (
              <div style={{marginBottom: 8}}>
                {listDom}
                {action}
              </div>
            )}
          >
            <ProFormText name="content" label="Lesson Content" placeholder="Enter lesson content"
                         rules={[{required: true}]}/>
            <ProFormDateTimePicker name="courseTime" label="Course Time" placeholder="Select course time"
                                   rules={[{required: true, message: "Please select the course time"}]}/>
          </ProFormList>
        </ProFormList>
      </ProForm>
    </Modal>

  )
}

export default CreatePlanModal;
