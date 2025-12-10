import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getIdentityDocumentReviews } from 'app/entities/identity-document-review/identity-document-review.reducer';
import { createEntity, getEntity, reset, updateEntity } from './document-review-observation.reducer';

export const DocumentReviewObservationUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const identityDocumentReviews = useAppSelector(state => state.identityDocumentReview.entities);
  const documentReviewObservationEntity = useAppSelector(state => state.documentReviewObservation.entity);
  const loading = useAppSelector(state => state.documentReviewObservation.loading);
  const updating = useAppSelector(state => state.documentReviewObservation.updating);
  const updateSuccess = useAppSelector(state => state.documentReviewObservation.updateSuccess);

  const handleClose = () => {
    navigate(`/document-review-observation${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getIdentityDocumentReviews({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    values.commentDate = convertDateTimeToServer(values.commentDate);

    const entity = {
      ...documentReviewObservationEntity,
      ...values,
      review: identityDocumentReviews.find(it => it.id.toString() === values.review?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          commentDate: displayDefaultDateTime(),
        }
      : {
          ...documentReviewObservationEntity,
          commentDate: convertDateTimeFromServer(documentReviewObservationEntity.commentDate),
          review: documentReviewObservationEntity?.review?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2
            id="monsterdamChicksApp.documentReviewObservation.home.createOrEditLabel"
            data-cy="DocumentReviewObservationCreateUpdateHeading"
          >
            <Translate contentKey="monsterdamChicksApp.documentReviewObservation.home.createOrEditLabel">
              Create or edit a DocumentReviewObservation
            </Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="document-review-observation-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('monsterdamChicksApp.documentReviewObservation.commentDate')}
                id="document-review-observation-commentDate"
                name="commentDate"
                data-cy="commentDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.documentReviewObservation.comment')}
                id="document-review-observation-comment"
                name="comment"
                data-cy="comment"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                id="document-review-observation-review"
                name="review"
                data-cy="review"
                label={translate('monsterdamChicksApp.documentReviewObservation.review')}
                type="select"
              >
                <option value="" key="0" />
                {identityDocumentReviews
                  ? identityDocumentReviews.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/document-review-observation" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default DocumentReviewObservationUpdate;
